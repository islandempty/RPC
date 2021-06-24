package com.zfoo.storage.model.vo;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author islandempty
 * @since 2021/6/24
 **/
public class Storage<K,V> {
    private Class<V> clazz;

    private final Map<K,V> dataMap = new HashMap<>();
    private final Map<String,Map<Object, List<V>>> indexMap = new HashMap<>();
    private final Map<String,Map<Object,V>> uniqueIndexMap = new HashMap<>();

    private IdDef idDef;
    private Map<String, IndexDef> indexDefMap;

    /**
     * 是否被使用
     */
    private boolean usable = false;

    public Storage(){}

    public void init(InputStream inputStream,Class<?> resourceClazz){
        this.clazz = (Class<V>) resourceClazz;

    }

    public int size() {
        return dataMap.size();
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }
}

