package com.zfoo.protocol.serializer;

import com.zfoo.protocol.registration.field.IFieldRegistration;
import io.netty.buffer.ByteBuf;

public interface ISerializer {

    void writeObject(ByteBuf buffer, Object object , IFieldRegistration fieldRegistration);

    Object readObject(ByteBuf buffer , IFieldRegistration fieldRegistration);
}
