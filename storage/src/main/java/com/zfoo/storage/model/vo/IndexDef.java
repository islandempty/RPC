package com.zfoo.storage.model.vo;

import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.storage.model.anno.Index;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 简化索引的名称，使用字段的名称作为索引的名称
 *
 * @author islandempty
 * @since 2021/6/24
 **/

public class IndexDef {

    private boolean unique;
    private Field field;

    public IndexDef(Field field) {
        ReflectionUtils.makeAccessible(field);
        this.field = field;
        var index = field.getAnnotation(Index.class);
        this.unique=index.unique();
    }
    public static Map<String , IndexDef> createResourceIndexes(Class<?> clazz){
        //获取clazz下所有带有index注解的字段
        var fields = ReflectionUtils.getFieldsByAnnoInPOJPClass(clazz, Index.class);
        var indexes = new ArrayList<IndexDef>(fields.length);

        for (var field : fields){
            IndexDef indexDef = new IndexDef(field);
            indexes.add(indexDef);
        }

        var result = new HashMap<String, IndexDef>();
        for (var index:indexes){
            var indexName = index.field.getName();
            if (result.put(indexName,index)!=null){
                throw new RuntimeException(StringUtils.format("资源类[{}]索引名称重复,索引名[}|]", clazz.getName(), indexName));
            }
        }
        return result;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}

