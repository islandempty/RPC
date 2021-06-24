package com.zfoo.storage.interpreter;

import java.io.InputStream;
import java.util.List;

/**
 *
 * 解释器模式设计的文本解析器
 *
 * @author islandempty
 * @since 2021/6/24
 **/
public interface IResourceReader {

    <T> List<T> read(InputStream inputStream,Class<T> clazz);
}

