package com.zfoo.storage.strategy;


import com.zfoo.protocol.util.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author islandempty
 * @since 2021/6/27
 **/
public class StringToDateConverter implements Converter<String , Date> {

    @Override
    public Date convert(String s) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return df.parse(s);
        } catch (ParseException e) {
            throw new IllegalArgumentException(StringUtils.format("字符串[{}]不符合格式要求:[yyyy-MM-dd HH:mm:ss]", s));
        }

    }
}

