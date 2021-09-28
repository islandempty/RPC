package com.zfoo.protocol.registration;

import com.zfoo.protocol.IPacket;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

public interface IProtocolRegistration {
    short protocolId();

    byte module();

    Constructor<?> protocolConstructor();

    /**
     * 协议接收器，回调方法，主要是存放一些额外的参数
     */
    Object receiver();

    /**
     * 序列化
     */
    void write(ByteBuf buffer, IPacket packet);

    /**
     * 反序列化
     */
    Object read(ByteBuf buffer);
}
