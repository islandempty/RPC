package com.zfoo.protocol.registration.field;

import com.zfoo.protocol.serializer.ISerializer;

/**
 * 一个包里所包含的变量还有这个变量的序列化器
 * 描述boolean，byte，short，int，long，float，double，char，String等基本序列化器
 *
 * @author islandempty
 * @since 2021/7/7
 **/
public class BaseField implements IFieldRegistration{

    private ISerializer serializer;

    public static BaseField valueOf(ISerializer serializer){
        BaseField baseField = new BaseField();
        baseField.serializer=serializer;
        return baseField;
    }

    @Override
    public ISerializer serializer() {
        return serializer;
    }
}

