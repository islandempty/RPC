package com.zfoo.protocol.serializer;

import com.zfoo.protocol.buffer.ByteBufUtils;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.registration.field.IFieldRegistration;
import com.zfoo.protocol.registration.field.SetField;
import io.netty.buffer.ByteBuf;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author islandempty
 * @since 2021/7/7
 **/
public class SetSerializer implements ISerializer{
    private static final SetSerializer SERIALIZER = new SetSerializer();


    private SetSerializer() {

    }

    public static SetSerializer getInstance() {
        return SERIALIZER;
    }

    @Override
    public void writeObject(ByteBuf buffer, Object object, IFieldRegistration fieldRegistration) {
        if (object == null) {
            ByteBufUtils.writeInt(buffer, 0);
            return;
        }

        Set<?> set = (Set<?>) object;
        SetField setField = (SetField) fieldRegistration;

        int size = set.size();
        if (size == 0) {
            ByteBufUtils.writeInt(buffer, 0);
            return;
        }
        ByteBufUtils.writeInt(buffer, size);

        for (Object element : set) {
            setField.getSetElementRegistration().serializer().writeObject(buffer, element, setField.getSetElementRegistration());
        }
    }

    @Override
    public Object readObject(ByteBuf buffer, IFieldRegistration fieldRegistration) {
        int size = ByteBufUtils.readInt(buffer);
        if (size <= 0) {
            return Collections.EMPTY_SET;
        }

        SetField setField = (SetField) fieldRegistration;
        Set<Object> set = new HashSet<>(CollectionUtils.comfortableCapacity(size));

        for (int i = 0; i < size; i++) {
            Object value = setField.getSetElementRegistration().serializer().readObject(buffer, setField.getSetElementRegistration());
            set.add(value);
        }

        return set;
    }
}

