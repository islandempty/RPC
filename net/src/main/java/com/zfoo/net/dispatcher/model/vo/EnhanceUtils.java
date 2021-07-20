package com.zfoo.net.dispatcher.model.vo;

import com.ie.util.security.IdUtils;
import com.zfoo.net.packet.model.IPacketAttachment;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.util.StringUtils;
import javassist.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public abstract class EnhanceUtils {

    static {
        var classArray = new Class<?>[]{
                IPacket.class,
                IPacketAttachment.class,
                IPacketReceiver.class,
                Session.class
        };

        var classPool = ClassPool.getDefault();

        for (var clazz : classArray){
            if (classPool.find(clazz.getCanonicalName()) == null){
                ClassClassPath classPath = new ClassClassPath(clazz);
                //添加到类搜索路径
                classPool.insertClassPath(classPath);
            }
        }
    }
    public static IPacketReceiver createPacketReceiver(PacketReceiverDefinition definition)throws NotFoundException, CannotCompileException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException{
        var classPool = ClassPool.getDefault();

        Object bean = definition.getBean();
        Method method = definition.getMethod();
        Class<?> packetClazz = definition.getPacketClazz();
        Class<?> attachmentClazz = definition.getAttachmentClazz();

        //定义类名称
        CtClass enhanceClazz = classPool.makeClass(EnhanceUtils.class.getCanonicalName() + "Dispatcher" + IdUtils.getLocalIntId());
        enhanceClazz.addInterface(classPool.get(IPacketReceiver.class.getCanonicalName()));

        //定义类中的一个成员
        CtField field = new CtField(classPool.get(bean.getClass().getCanonicalName()), "bean", enhanceClazz);
        field.setModifiers(Modifier.PRIVATE);
        enhanceClazz.addField(field);

        //定义类构造器 参数--
        CtConstructor constructor = new CtConstructor(classPool.get(new String[]{bean.getClass().getCanonicalName()}), enhanceClazz);
        constructor.setBody("{this.bean=$1;}");
        constructor.setModifiers(Modifier.PUBLIC);
        enhanceClazz.addConstructor(constructor);

        //定义类实现的接口方法
        CtMethod invokeMethod = new CtMethod(classPool.get(void.class.getCanonicalName()), "invoke", classPool.get(new String[]{Session.class.getCanonicalName(), IPacket.class.getCanonicalName(), IPacketAttachment.class.getCanonicalName()}), enhanceClazz);
        invokeMethod.setModifiers(Modifier.PUBLIC + Modifier.FINAL);
        if (attachmentClazz == null){
            //强制类型转换
            String invokeMethodBody = StringUtils.format("{this.bean.{}($1, ({})$2);}", method.getName(), packetClazz.getCanonicalName());
            invokeMethod.setBody(invokeMethodBody);
        }else {
            String invokeMethodBody = StringUtils.format("{this.bean.{}($1, ({})$2, ({})$3);}", method.getName(), packetClazz.getCanonicalName(), attachmentClazz.getCanonicalName());
            invokeMethod.setBody(invokeMethodBody);
        }
        enhanceClazz.addMethod(invokeMethod);

        //释放缓存
        enhanceClazz.detach();

        Class<?> resultClazz = enhanceClazz.toClass(IPacketReceiver.class);
        Constructor<?> resultConstructor = resultClazz.getConstructor(bean.getClass());
        IPacketReceiver o = (IPacketReceiver)resultConstructor.newInstance(bean);
        return o;
    }


}

