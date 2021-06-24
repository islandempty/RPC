package com.zfoo.storage.strategy;

import com.zfoo.protocol.util.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @author islandempty
 * @since 2021/6/24
 **/
public class JsonToArrayConverter implements ConditionalGenericConverter {

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.getType() == String.class && targetType.getType().isArray();
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Object.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        Class<?> clazz = null;

        String content = (String) source;
        String targetClazzName = targetType.getObjectType().getName();
        if (targetClazzName.contains(StringUtils.LEFT_SQUARE_BRACKET)||targetClazzName.contains(StringUtils.SEMICOLON)){
            String clazzPath = targetClazzName.substring(2, targetClazzName.length() - 1);
            try {
                clazz = Class.forName(clazzPath);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            clazz = targetType.getObjectType();
        }
        return null;
    }
}

