package com.zfoo.storage.strategy;


import com.zfoo.protocol.util.JsonUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;

/**
 * @author islandempty
 * @since 2021/6/27
 **/
public class StringToMapConverter implements Converter<String, Map<String,Object>> {
    @Override
    public Map<String, Object> convert(String s) {
        return JsonUtils.string2Map(s,String.class,Object.class);
    }
}

