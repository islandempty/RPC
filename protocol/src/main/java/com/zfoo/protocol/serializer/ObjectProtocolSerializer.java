package com.zfoo.protocol.serializer;

import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.ProtocolManager;
import com.zfoo.protocol.registration.IProtocolRegistration;
import com.zfoo.protocol.registration.field.IFieldRegistration;
import com.zfoo.protocol.registration.field.ObjectProtocolField;
import io.netty.buffer.ByteBuf;

/**
 * @author islandempty
 * @since 2021/7/7
 **/
public class ObjectProtocolSerializer implements ISerializer{
    private static final ObjectProtocolSerializer SERIALIZER = new ObjectProtocolSerializer();

    private ObjectProtocolSerializer() {

    }

    public static ObjectProtocolSerializer getInstance() {
        return SERIALIZER;
    }

    /**
     * @param buffer ByteBuf
     * @param object 必须继承IPacket接口
     */
    @Override
    public void writeObject(ByteBuf buffer, Object object, IFieldRegistration fieldRegistration) {
        ObjectProtocolField objectProtocolField = (ObjectProtocolField) fieldRegistration;
        IProtocolRegistration protocol = ProtocolManager.getProtocol(objectProtocolField.getProtocolId());
        protocol.write(buffer, (IPacket) object);
    }

    @Override
    public Object readObject(ByteBuf buffer, IFieldRegistration fieldRegistration) {
        ObjectProtocolField objectProtocolField = (ObjectProtocolField) fieldRegistration;
        IProtocolRegistration protocol = ProtocolManager.getProtocol(objectProtocolField.getProtocolId());
        return protocol.read(buffer);
    }
}

