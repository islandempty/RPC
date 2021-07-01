package com.zfoo.storage.interpreter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.storage.StorageContext;
import com.zfoo.storage.model.anno.Id;
import com.zfoo.storage.util.CellUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.convert.TypeDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author islandempty
 * @since 2021/6/24
 **/
public class ExcelResourceReader implements IResourceReader{
    private static final TypeDescriptor TYPE_DESCRIPTOR  = TypeDescriptor.valueOf(String.class);

    @Override
    public <T> List<T> read(InputStream inputStream, Class<T> clazz) {
        var workbook = createWorkbook(inputStream, clazz);
        var result = new ArrayList<T>();

        //默认读取到第一个sheet页
        var sheet =workbook.getSheetAt(0);
        var fieldInfos = getFieldInfos(sheet,clazz);

        var iterator = sheet.iterator();
        //行数定位到有效数据行,默认是第四行为有效数据行
        iterator.next();
        iterator.next();
        iterator.next();

        //从ROW_SERVER这行开始读取数据
        while (iterator.hasNext()){
            var row =iterator.next();
            var instance = ReflectionUtils.newInstance(clazz);

            var idCell = row.getCell(0);
            if (idCell == null || StringUtils.isBlank(CellUtils.getCellStringValue(idCell))){
                continue;
            }

            for (var fieldInfo : fieldInfos){
                var cell = row.getCell(fieldInfo.index);
                if (cell != null){
                    var content = CellUtils.getCellStringValue(cell);
                    if (!StringUtils.isEmpty(content)){
                        inject(instance,fieldInfo.field,content);
                    }
                }

                //如果读的是id列的单元格，则判断当前id是否为空
                if (fieldInfo.field.isAnnotationPresent(Id.class)){
                    if (cell == null || StringUtils.isEmpty(CellUtils.getCellStringValue(cell))){
                        throw new RuntimeException(StringUtils.format("静态资源[resource:{}]存在id未配置的项", clazz.getSimpleName()));
                    }
                }
                result.add(instance);
            }
            return result;
        }
        return null;
    }
    public void inject(Object instance, Field field ,String content){
        try {
            var targetType = new TypeDescriptor(field);
            var value = StorageContext.getConversionService().convert(content, TYPE_DESCRIPTOR, targetType);
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field,instance,value);
        }catch (Exception e){
            throw new RuntimeException(StringUtils.format("无法将Excel资源[class:{}]中的[content:{}]转换为属性[field:{}]"
                    , instance.getClass().getSimpleName(), content, field.getName()), e);
        }
    }

    //只读取代码里的字段
    private Collection<FieldInfo> getFieldInfos(Sheet sheet,Class<?> clazz){
        var fieldRow = getFieldRow(sheet);
        if (fieldRow == null){
            throw new RuntimeException(StringUtils.format("无法获取资源[class:{}]的Excel文件的属性控制列", clazz.getSimpleName()));
        }

        var cellFieldMap =new HashMap<String,Integer>();
        for (var i=0;i<fieldRow.getLastCellNum();i++){
            var cell =fieldRow.getCell(i);
            if (Objects.isNull(cell)){
                continue;
            }

            var name = CellUtils.getCellStringValue(cell);
            if (StringUtils.isEmpty(name)){
                continue;
            }

            var previousValue = cellFieldMap.put(name , i);
            if (Objects.nonNull(previousValue)){
                throw new RuntimeException(StringUtils.format("资源[class:{}]的Excel文件出现重复的属性控制列[field:{}]"
                        , clazz.getSimpleName(), name));
            }
        }
        var fieldList = Arrays.stream(clazz.getDeclaredFields())
                .filter(it->!Modifier.isTransient(it.getModifiers()))
                .filter(it->!Modifier.isStatic(it.getModifiers()))
                .collect(Collectors.toList());
        for (var field : fieldList) {
            if (!cellFieldMap.containsKey(field.getName())) {
                throw new RuntimeException(StringUtils.format("资源类[class:{}]的声明属性[filed:{}]无法获取，请检查配置表的格式", clazz, field.getName()));
            }
        }

        return fieldList.stream().map(it -> new FieldInfo(cellFieldMap.get(it.getName()), it)).collect(Collectors.toList());
    }

    //获取配置表的有效列名称
    private Row getFieldRow(Sheet sheet){
        var iterator = sheet.iterator();
        var row = iterator.next();
        return row;
    }

    private Workbook createWorkbook(InputStream inputStream,Class<?> clazz){
        try {
            return WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(StringUtils.format("静态资源[{}]异常，无法读取文件", clazz.getSimpleName()));
        }
    }

    public static class FieldInfo{
        public final int index;
        public final Field field;

        public FieldInfo(int index ,Field field){
            this.index = index;
            this.field = field;
        }
    }
}

