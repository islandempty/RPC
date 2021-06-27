package com.zfoo.storage.strategy;

import com.zfoo.protocol.util.StringUtils;
import com.zfoo.storage.StorageContext;
import org.springframework.core.convert.converter.Converter;

/**
 * @author islandempty
 * @since 2021/6/27
 **/
public class StringToClassConverter implements Converter<String , Class<?>> {
    @Override
    public Class<?> convert(String source) {
        if (!source.contains(".")&&!source.startsWith("[")){
            source="java.lang."+source;
        }

        ClassLoader loader = null;

        StorageContext context = StorageContext.getInstance();

        if (context!=null){
            loader = StorageContext.getApplicationContext().getClassLoader();
        }else {
            loader = Thread.currentThread().getContextClassLoader();
        }

        try {
            return Class.forName(source,true,loader);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(StringUtils.format("无法将字符串[{}]转换为Class对象", source));
        }

    }

}

