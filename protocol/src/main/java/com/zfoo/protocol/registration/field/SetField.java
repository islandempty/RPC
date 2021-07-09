package com.zfoo.protocol.registration.field;

import com.zfoo.protocol.serializer.ISerializer;
import com.zfoo.protocol.serializer.SetSerializer;

import java.lang.reflect.Type;

/**
 * @author islandempty
 * @since 2021/7/7
 **/
public class SetField implements IFieldRegistration{
    private IFieldRegistration setElementRegistration;
    private Type type;

    public static SetField valueOf(IFieldRegistration listElementRegistration, Type type) {
        SetField setField = new SetField();
        setField.setElementRegistration = listElementRegistration;
        setField.type = type;
        return setField;
    }

    @Override
    public ISerializer serializer() {
        return SetSerializer.getInstance();
    }

    public IFieldRegistration getSetElementRegistration() {
        return setElementRegistration;
    }

    public Type getType() {
        return type;
    }
}

