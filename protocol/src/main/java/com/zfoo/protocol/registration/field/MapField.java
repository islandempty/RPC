package com.zfoo.protocol.registration.field;

import com.zfoo.protocol.serializer.ISerializer;
import com.zfoo.protocol.serializer.MapSerializer;

import java.lang.reflect.Type;

/**
 * @author islandempty
 * @since 2021/7/7
 **/
public class MapField implements IFieldRegistration{
    private IFieldRegistration mapKeyRegistration;
    private IFieldRegistration mapValueRegistration;

    private Type type;

    public static MapField valueOf(IFieldRegistration mapKeyRegistration, IFieldRegistration mapValueRegistration, Type type) {
        MapField mapField = new MapField();
        mapField.mapKeyRegistration = mapKeyRegistration;
        mapField.mapValueRegistration = mapValueRegistration;
        mapField.type = type;
        return mapField;
    }


    @Override
    public ISerializer serializer() {
        return MapSerializer.getInstance();
    }

    public IFieldRegistration getMapKeyRegistration() {
        return mapKeyRegistration;
    }

    public IFieldRegistration getMapValueRegistration() {
        return mapValueRegistration;
    }

    public Type getType() {
        return type;
    }
}

