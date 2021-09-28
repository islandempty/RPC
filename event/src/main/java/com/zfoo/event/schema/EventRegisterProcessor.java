package com.zfoo.event.schema;

import com.zfoo.event.manager.EventBus;
import com.zfoo.event.model.anno.EventReceiver;
import com.zfoo.event.model.event.IEvent;
import com.zfoo.event.model.vo.EnhanceUtils;
import com.zfoo.event.model.vo.EventReceiverDefinition;
import com.zfoo.protocol.collection.ArrayUtils;
import com.zfoo.protocol.exception.RunException;
import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.protocol.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;


import java.lang.reflect.Modifier;

/**
 * @author islandempty
 * @since 2021/6/30
 **/
public class EventRegisterProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EventRegisterProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        var clazz = bean.getClass();
        //找出所有被EventReceiver注解的方法
        var methods = ReflectionUtils.getMethodsByAnnoInPOJOClass(clazz, EventReceiver.class);
        if (ArrayUtils.isEmpty(methods)){
            return bean;
        }

        if (!ReflectionUtils.isPojoClass(clazz)){
            logger.warn("事件注册类[{}]不是POJO类，父类的事件接收不会被扫描到",clazz);
        }

        try {
            for (var method:methods){
                //获得所有参数类型
                var parameterTypes = method.getParameterTypes();
                if (parameterTypes.length !=1){
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] must have one parameter!", bean.getClass().getName(), method.getName()));
                }

                if (!IEvent.class.isAssignableFrom(parameterTypes[0])){
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] must have one [IEvent] type parameter!", bean.getClass().getName(), method.getName()));
                }
                //参数的class
                var eventClazz = (Class<? extends IEvent>)parameterTypes[0];
                var eventName = eventClazz.getCanonicalName();
                var methodName = method.getName();

                if (!Modifier.isPublic(method.getModifiers())){
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] [event:{}] must use 'public' as modifier!", bean.getClass().getName(), methodName, eventName));
                }
                if (Modifier.isStatic(method.getModifiers())){
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] [event:{}] can not use 'static' as modifier!", bean.getClass().getName(), methodName, eventName));
                }

                var expectedMethodName = StringUtils.format("on{}", eventClazz.getSimpleName());
                if (!methodName.equals(expectedMethodName)){
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] [event:{}] expects '{}' as method name!"
                            , bean.getClass().getName(), methodName, eventName, expectedMethodName));
                }

                //生成bean信息
                var receiverDefinition = new EventReceiverDefinition(bean, method, eventClazz);
                //根据BeanDefinition生成IEventReceiver实例
                var eventReceiver = EnhanceUtils.createEventReceiver(receiverDefinition);
                //加入总线任务
                EventBus.registerEventReceiver(eventClazz,eventReceiver);
            }
        } catch (Throwable t) {
            throw new RunException(t);
        }

        return bean;
    }
}

