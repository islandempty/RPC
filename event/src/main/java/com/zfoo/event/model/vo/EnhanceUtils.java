package com.zfoo.event.model.vo;

import com.zfoo.util.security.IdUtils;
import com.zfoo.event.model.event.IEvent;
import com.zfoo.event.schema.NamespaceHandler;
import com.zfoo.protocol.util.StringUtils;
import javassist.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author islandempty
 * @since 2021/6/30
 **/
public abstract class EnhanceUtils {

    static {
        //适配Tomcat，因为Tomcat不是用的默认的类加载器，而Javaassist用的是默认的加载器
        var classArray = new Class<?>[]{
                IEventReceiver.class,
                IEvent.class
        };

        var classPool = ClassPool.getDefault();

        for (var clazz : classArray){
            if (classPool.find(clazz.getCanonicalName())==null){
                ClassClassPath classPath = new ClassClassPath(clazz);
                classPool.insertClassPath(classPath);
            }
        }
    }

    public static IEventReceiver createEventReceiver(EventReceiverDefinition definition)throws NotFoundException, CannotCompileException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException{
        var classPool = ClassPool.getDefault();

        Object bean = definition.getBean();
        Method method = definition.getMethod();
        Class<?> clazz = definition.getEventClazz();

        //定义类名称
        CtClass enhanceClazz = classPool.makeClass(EnhanceUtils.class.getCanonicalName()+ StringUtils.capitalize(NamespaceHandler.EVENT) + IdUtils.getLocalIntId());
        enhanceClazz.addInterface(classPool.get(IEventReceiver.class.getCanonicalName()));

        //定义类中的一个成员
        CtField field = new CtField(classPool.get(bean.getClass().getCanonicalName()), "bean", enhanceClazz);
        field.setModifiers(Modifier.PRIVATE);
        enhanceClazz.addField(field);

        //定义类的构造器
        CtConstructor constructor = new CtConstructor(classPool.get(new String[]{bean.getClass().getCanonicalName()}), enhanceClazz);
        constructor.setBody("{this.bean=$1;}");
        constructor.setModifiers(Modifier.PUBLIC);
        enhanceClazz.addConstructor(constructor);

        //定义类实现的接口方法
        CtMethod invokeMethod = new CtMethod(classPool.get(void.class.getCanonicalName()), "invoke", classPool.get(new String[]{IEvent.class.getCanonicalName()}), enhanceClazz);
        invokeMethod.setModifiers(Modifier.PUBLIC + Modifier.FINAL);
        // 强制类型转换，转换为具体的Event类型的类型
        String invokeMethodBody = "{this.bean."+method.getName()+"(("+clazz.getCanonicalName()+")$1);}";
        invokeMethod.setBody(invokeMethodBody);
        enhanceClazz.addMethod(invokeMethod);

        //释放缓存
        enhanceClazz.detach();

        Class<?> aClass = enhanceClazz.toClass(IEventReceiver.class);
        Constructor<?> aClassConstructor = aClass.getConstructor(bean.getClass());
        IEventReceiver instance = (IEventReceiver)aClassConstructor.newInstance(bean);
        return instance;
    }
}

