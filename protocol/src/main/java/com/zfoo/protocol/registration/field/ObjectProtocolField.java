package com.zfoo.protocol.registration.field;

import com.zfoo.protocol.serializer.ISerializer;
import com.zfoo.protocol.serializer.ObjectProtocolSerializer;

/**
 * @author islandempty
 * @since 2021/7/7
 **/
public class ObjectProtocolField implements IFieldRegistration{
    /**
     * 协议序列号是ProtocolRegistration的id
     */
    private short protocolId;

    public static ObjectProtocolField valueOf(short protocolId) {
        ObjectProtocolField objectProtocolField = new ObjectProtocolField();
        objectProtocolField.protocolId = protocolId;
        return objectProtocolField;
    }

    public short getProtocolId() {
        return protocolId;
    }

    @Override
    public ISerializer serializer() {
        return ObjectProtocolSerializer.getInstance();
    }
}

