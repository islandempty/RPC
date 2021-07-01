package com.zfoo.event.model.vo;

import com.zfoo.event.model.event.IEvent;
import com.zfoo.protocol.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author islandempty
 * @since 2021/6/29
 **/
public class EventReceiverDefinition implements IEventReceiver{
    private Object bean;

    //被ReceiveEvent注解的办法
    private Method method;

    //接收参数的Class
    private Class<? extends IEvent> eventClazz;

    public EventReceiverDefinition(Object bean, Method method, Class<? extends IEvent> eventClazz) {
        this.bean = bean;
        this.method = method;
        this.eventClazz = eventClazz;
        ReflectionUtils.makeAccessible(this.method);
    }


    @Override
    public void invoke(IEvent event) {
        ReflectionUtils.invokeMethod(bean,method,event);
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<? extends IEvent> getEventClazz() {
        return eventClazz;
    }

    public void setEventClazz(Class<? extends IEvent> eventClazz) {
        this.eventClazz = eventClazz;
    }
}

