package com.zfoo.storage.model.vo;

import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.protocol.util.IOUtils;
import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.storage.StorageContext;

import java.io.InputStream;
import java.util.*;

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
        try {
            this.clazz = (Class<V>) resourceClazz;
            var reader = StorageContext.getResourceReader();
            idDef = IdDef.valueOf(resourceClazz);
            indexDefMap = IndexDef.createResourceIndexes(resourceClazz);

            var list = reader.read(inputStream, resourceClazz);

            dataMap.clear();
            indexMap.clear();
            uniqueIndexMap.clear();
            for (var object : list){
                put((V)object);
            }
        }catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            IOUtils.closeIO(inputStream);
        }
    }

    public Collection<V> getAll(){
        return Collections.unmodifiableCollection(dataMap.values());
    }

    public V get(K id){
        V result = dataMap.get(id);
        AssertionUtils.notNull(result,"静态资源[resource:{}]种表示为[id:{}]的静态资源不存在", clazz.getSimpleName(), id);
        return (V) result;
    }

    public List<V> getIndex(String indexName, Object key){
        var indexValues = indexMap.get(indexName);
        AssertionUtils.notNull(indexValues,"静态资源[resource:{}]不存在为[indexName:{}]的索引", clazz.getSimpleName(), indexName);
        var values = indexValues.get(key);
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        return values;
    }

    public V getUniqueIndex(String uniqueIndexName, Object key){
        var indexValueMap = uniqueIndexMap.get(uniqueIndexName);
        AssertionUtils.notNull(indexValueMap, "静态资源[resource:{}]不存在为[uniqueIndexName:{}]的唯一索引", clazz.getSimpleName(), uniqueIndexName);
        var value = indexValueMap.get(key);
        return value;
    }

    private V put(V value){
        var key = (K) ReflectionUtils.getField(idDef.getField(),value);

        if (key == null){
            throw new RuntimeException("静态资源存在id未配置的项");
        }

        if (dataMap.containsKey(key)){
            throw new RuntimeException(StringUtils.format("静态资源[resource:{}]的[id:{}]重复", clazz.getSimpleName(), key));
        }

        //添加资源
        V result = dataMap.put(key, value);

        //添加索引
        for (var def : indexDefMap.values()){
            //使用field的名称作为索引的名称
            var indexKey = def.getField().getName();
            var indexValue = ReflectionUtils.getField(def.getField(), value);
            if (def.isUnique()){
                //它当键不存在时或者键存在但是值为null时，后面的Function类才会起到作用
                var index = uniqueIndexMap.computeIfAbsent(indexKey, k->new HashMap<>());
                if (index.put(indexValue,value)!=null){
                    throw new RuntimeException(StringUtils.format("静态资源[class:{}]的唯一索引重复[index:{}][value:{}]", clazz.getName(), indexKey, indexValue));
                }
            }else {
                //不是唯一索引
                var index = indexMap.computeIfAbsent(indexKey,k->new HashMap<>());
                var list = index.computeIfAbsent(indexValue,k->new ArrayList<V>());
                list.add(value);
            }
        }
        return result;
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

