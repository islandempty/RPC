package com.zfoo.protocol.serializer;

import com.zfoo.protocol.buffer.ByteBufUtils;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.registration.field.IFieldRegistration;
import com.zfoo.protocol.registration.field.MapField;
import io.netty.buffer.ByteBuf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author islandempty
 * @since 2021/7/7
 **/
public class MapSerializer implements ISerializer{

    private static final MapSerializer SERIALIZER = new MapSerializer();


    private MapSerializer() {

    }

    public static MapSerializer getInstance() {
        return SERIALIZER;
    }

    @Override
    public void writeObject(ByteBuf buffer, Object object, IFieldRegistration fieldRegistration) {
        if (object == null) {
            ByteBufUtils.writeInt(buffer, 0);
            return;
        }

        Map<?, ?> map = (Map<?, ?>) object;
        MapField mapField = (MapField) fieldRegistration;

        int size = map.size();
        if (size == 0) {
            ByteBufUtils.writeInt(buffer, 0);
            return;
        }
        ByteBufUtils.writeInt(buffer, size);

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            mapField.getMapKeyRegistration().serializer().writeObject(buffer, entry.getKey(), mapField.getMapKeyRegistration());

            mapField.getMapValueRegistration().serializer().writeObject(buffer, entry.getValue(), mapField.getMapValueRegistration());
        }
    }

    @Override
    public Object readObject(ByteBuf buffer, IFieldRegistration fieldRegistration) {
        int size = ByteBufUtils.readInt(buffer);
        if (size <= 0) {
            return Collections.EMPTY_MAP;
        }

        MapField mapField = (MapField) fieldRegistration;
        Map<Object, Object> map = new HashMap<>(CollectionUtils.comfortableCapacity(size));

        for (int i = 0; i < size; i++) {
            Object key = mapField.getMapKeyRegistration().serializer().readObject(buffer, mapField.getMapKeyRegistration());

            Object value = mapField.getMapValueRegistration().serializer().readObject(buffer, mapField.getMapValueRegistration());

            map.put(key, value);
        }
        return map;
    }
}

