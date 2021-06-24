package com.zfoo.storage.interpreter;

import java.io.InputStream;
import java.util.List;

/**
 * @author islandempty
 * @since 2021/6/24
 **/
public class ExcelResourceReader implements IResourceReader{
    @Override
    public <T> List<T> read(InputStream inputStream, Class<T> clazz) {
        return null;
    }
}

