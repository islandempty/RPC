package com.zfoo.scheduler.model.vo;

import com.zfoo.protocol.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author islandempty
 * @since 2021/7/2
 **/
public class ReflectScheduler implements IScheduler{

    private Object bean;
    private Method method;

    public static ReflectScheduler valueOf(Object bean,Method method){
        var scheduler = new ReflectScheduler();
        scheduler.bean=bean;
        scheduler.method=method;
        return scheduler;
    }
    @Override
    public void invoke() {
        ReflectionUtils.invokeMethod(bean,method);
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }
}

