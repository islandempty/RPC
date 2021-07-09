package com.zfoo.protocol.serializer;

import com.zfoo.protocol.buffer.ByteBufUtils;
import com.zfoo.protocol.registration.field.ArrayField;
import com.zfoo.protocol.registration.field.IFieldRegistration;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * @author islandempty
 * @since 2021/7/6
 **/
public class ArraySerializer implements ISerializer{

    private static final ArraySerializer SERIALIZER = new ArraySerializer();

    private ArraySerializer(){

    }

    public static ArraySerializer getInstance(){
        return SERIALIZER;
    }
    @Override
    public void writeObject(ByteBuf buffer, Object object, IFieldRegistration fieldRegistration) {
        if (object == null) {
            ByteBufUtils.writeInt(buffer, 0);
            return;
        }

        ArrayField arrayField = (ArrayField) fieldRegistration;

        int length = Array.getLength(object);
        if (length == 0) {
            ByteBufUtils.writeInt(buffer, 0);
            return;
        }

        ByteBufUtils.writeInt(buffer, length);

        for (int i = 0; i < length; i++) {
            Object element = Array.get(object, i);
            arrayField.getArrayElementRegistration().serializer().writeObject(buffer, element, arrayField.getArrayElementRegistration());
        }
    }

    @Override
    public Object readObject(ByteBuf buffer, IFieldRegistration fieldRegistration) {
        var length = ByteBufUtils.readInt(buffer);
        ArrayField arrayField = (ArrayField) fieldRegistration;
        if (length <= 0) {
            return Array.newInstance(arrayField.getField().getType().getComponentType(), 0);
        }

        Object array = Array.newInstance(arrayField.getField().getType().getComponentType(), length);

        for (var i = 0; i < length; i++) {
            Object value = arrayField.getArrayElementRegistration().serializer().readObject(buffer, arrayField.getArrayElementRegistration());
            Array.set(array, i, value);
        }

        return array;
    }
}

