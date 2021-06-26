package com.zfoo.storage.strategy;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.protocol.util.ReflectionUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @author islandempty
 * @since 2021/6/26
 **/
public class JsonToObjectConverter implements ConditionalGenericConverter {


    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (sourceType.getType()!= String.class){
            return false;
        }

        //判断是否是基本类型
        if (targetType.getType().isPrimitive()){
            return false;
        }

        //判断目标类型是否是数字
        if (Number.class.isAssignableFrom(targetType.getType())){
            return false;
        }

        //判断模板类型是否是字符
        if (CharSequence.class.isAssignableFrom(targetType.getType())){
            return false;
        }


        return ReflectionUtils.isPOJOClass(targetType.getType());
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class,Object.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        String content = (String) source;
        return JsonUtils.string2Object(content,targetType.getType());
    }
}

