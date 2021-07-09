package com.zfoo.protocol.registration.field;

import com.zfoo.protocol.serializer.ISerializer;
import com.zfoo.protocol.serializer.ListSerializer;

import java.lang.reflect.Type;

/**
 * @author islandempty
 * @since 2021/7/7
 **/
public class ListField implements IFieldRegistration{
    private IFieldRegistration listElementRegistration;
    private Type type;

    public static ListField valueOf(IFieldRegistration listElementRegistration, Type type) {
        ListField listField = new ListField();
        listField.listElementRegistration = listElementRegistration;
        listField.type = type;
        return listField;
    }

    @Override
    public ISerializer serializer() {
        return ListSerializer.getInstance();
    }

    public IFieldRegistration getListElementRegistration() {
        return listElementRegistration;
    }

    public Type getType() {
        return this.type;
    }

}

