package com.zfoo.storage.model.vo;

import com.zfoo.protocol.exception.RunException;
import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.storage.model.anno.Id;

import java.lang.reflect.Field;

/**
 * @author islandempty
 * @since 2021/6/23
 **/
public class IdDef {

    private Field field;

    public static IdDef valueOf(Class<?> clazz){
        //获取所有具有Id注解的字段
        Field[] fields = ReflectionUtils.getFieldsByAnnoInPOJPClass(clazz, Id.class);
        if (fields.length <= 0){
            throw new RunException("class[{}]没有主键Id注解", clazz.getName());
        }
        if (fields.length > 1){
            throw new RunException("类[{}]的主键Id注解重复", clazz.getName());
        }
        if (fields[0] == null) {
            throw new RunException("不合法的Id资源映射对象：" + clazz.getName());
        }
        Field idField = fields[0];
        //字段变成公有
        ReflectionUtils.makeAccessible(idField);
        IdDef idDef = new IdDef();
        idDef.setField(idField);
        return idDef;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}

