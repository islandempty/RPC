package com.zfoo.scheduler.schema;

import com.zfoo.protocol.collection.ArrayUtils;
import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.scheduler.manager.SchedulerBus;
import com.zfoo.scheduler.model.anno.Scheduler;
import com.zfoo.scheduler.model.vo.SchedulerDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Modifier;

/**
 * @author islandempty
 * @since 2021/7/2
 **/
public class SchedulerRegisterProcessor implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerRegisterProcessor.class);
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        var clazz = bean.getClass();
        var methods = ReflectionUtils.getMethodsByAnnoInPOJOClass(bean.getClass(), Scheduler.class);

        if (ArrayUtils.isEmpty(methods)){
            return bean;
        }

        if (!ReflectionUtils.isPOJOClass(clazz)){
            logger.warn("调度注册类[{}]不是POJO类，父类的调度不会被扫描到", clazz);
        }

        try {
            for (var method : methods){
                var schedulerMethod = method.getAnnotation(Scheduler.class);

                var paramClazzs = method.getParameterTypes();
                if (paramClazzs.length >= 1){
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] can not have any parameters", bean.getClass(), method.getName()));
                }

                var methodName = method.getName();
                if (!Modifier.isPublic(method.getModifiers())){
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] must use 'public' as modifier!", bean.getClass().getName(), methodName));
                }
                if (Modifier.isStatic(method.getModifiers())){
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] can not use 'static' as modifier!", bean.getClass().getName(), methodName));
                }
                if (!methodName.startsWith("cron")){
                    throw new IllegalArgumentException(StringUtils.format("[class:{}] [method:{}] must start with 'cron' as method name!"
                            , bean.getClass().getName(), methodName));
                }

                var scheduler = SchedulerDefinition.valueOf(schedulerMethod.cron(),bean,method);
                SchedulerBus.registerScheduler(scheduler);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return bean;
    }
}

