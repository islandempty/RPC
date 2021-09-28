package com.zfoo.net.dispatcher.model.vo;

import com.zfoo.net.packet.model.IPacketAttachment;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public class PacketReceiverDefinition implements IPacketReceiver{

    /**
     * 一个facade的bean，这个bean里有void methodName(Session session,CM_Int cm)接受的方法
     */
    private Object bean;

    /**
     * 接受的方法void methodName(Session session,CM_Int cm)
     */
    private Method method;

    /**
     * 接收的包的Class类，如CM_Int
     */
    private Class<?> packetClazz;

    /**
     * 接收的包的附加包的Class类，如GatewayPacketAttachment
     */
    private Class<?> attachmentClazz;

    public PacketReceiverDefinition(Object bean, Method method, Class<?> packetClazz, Class<?> attachmentClazz) {
        this.bean = bean;
        this.method = method;
        this.packetClazz = packetClazz;
        this.attachmentClazz = attachmentClazz;
        ReflectionUtils.makeAccessible(method);
    }

    @Override
    public void invoke(Session session, IPacket Packet, IPacketAttachment attachment) {
        if (attachmentClazz == null){
            ReflectionUtils.invokeMethod(bean, method, session, Packet);
        }else {
            ReflectionUtils.invokeMethod(bean, method, session, Packet, attachment);
        }
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

    public Class<?> getPacketClazz() {
        return packetClazz;
    }

    public void setPacketClazz(Class<?> packetClazz) {
        this.packetClazz = packetClazz;
    }

    public Class<?> getAttachmentClazz() {
        return attachmentClazz;
    }

    public void setAttachmentClazz(Class<?> attachmentClazz) {
        this.attachmentClazz = attachmentClazz;
    }
}

