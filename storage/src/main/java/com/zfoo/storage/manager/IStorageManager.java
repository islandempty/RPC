package com.zfoo.storage.manager;

import com.zfoo.storage.model.vo.Storage;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Set;

public interface IStorageManager {

    /**
     * 配置表初始化之前，先读取所有的excel
     */
    void initBefore();

    /**
     * 程序加载过后，移除没有用到的配置表
     */
    void initAfter();

    @Nullable
    Storage<?,?> getStorage(Class<?> clazz);

    Set<Class<?>> allStorageClassSet();

    Map<Class<?>,Storage<?,?>> getStorageMap();

    void updateStorage(Class<?> clazz,Storage<?,?> storage);
}
