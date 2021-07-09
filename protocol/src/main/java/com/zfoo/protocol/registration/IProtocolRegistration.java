package com.zfoo.protocol.registration;

import com.zfoo.protocol.IPacket;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

public interface IProtocolRegistration {
    short protocolId();

    byte module();

    Constructor<?> protocolConstructor();

    Object read(ByteBuf buffer);

    void write(ByteBuf buffer , IPacket packet);
}
