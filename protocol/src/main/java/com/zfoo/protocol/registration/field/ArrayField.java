package com.zfoo.protocol.registration.field;

import com.zfoo.protocol.serializer.ArraySerializer;
import com.zfoo.protocol.serializer.ISerializer;

import java.lang.reflect.Field;

/**
 * @author islandempty
 * @since 2021/7/5
 **/
public class ArrayField implements IFieldRegistration{
    private IFieldRegistration arrayElementRegistration;
    private Field field;

    public static ArrayField valueOf(Field field,IFieldRegistration arrayElementRegistration){
        ArrayField arrayField = new ArrayField();
        arrayField.field=field;
        arrayField.arrayElementRegistration=arrayElementRegistration;
        return arrayField;
    }
    public Field getField(){
        return field;
    }
    public IFieldRegistration getArrayElementRegistration(){
        return arrayElementRegistration;
    }
    @Override
    public ISerializer serializer() {
        return ArraySerializer.getInstance();
    }
}

