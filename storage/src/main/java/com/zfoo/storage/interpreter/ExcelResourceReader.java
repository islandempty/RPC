package com.zfoo.storage.interpreter;

import org.springframework.core.convert.TypeDescriptor;

import java.io.InputStream;
import java.util.List;

/**
 * @author islandempty
 * @since 2021/6/24
 **/
public class ExcelResourceReader implements IResourceReader{
    private static final TypeDescriptor TYPE_DESCRIPTOR  = TypeDescriptor.valueOf(String.class);

    @Override
    public <T> List<T> read(InputStream inputStream, Class<T> clazz) {
        return null;
    }
}

