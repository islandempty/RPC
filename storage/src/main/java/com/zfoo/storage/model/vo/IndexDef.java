package com.zfoo.storage.model.vo;

import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.storage.model.anno.Index;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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

