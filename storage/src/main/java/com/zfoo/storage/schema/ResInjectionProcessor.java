package com.zfoo.storage.schema;

import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.storage.StorageContext;
import com.zfoo.storage.model.anno.Id;
import com.zfoo.storage.model.anno.ResInjection;
import com.zfoo.storage.model.vo.Storage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Bean级生命周期接口和容器生命周期接口是个性和共性的辩证统一思想的体现，
 * 前者解决Bean个性化处理的问题，而后者解决容器中某些Bean共性处理的问题
 * @author islandempty
 * @since 2021/6/24
 **/

//bean的前置后置处理器
public class ResInjectionProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StorageContext.getStorageContext() == null){
            return bean;
        }

        //将bean.getClass()通过filter过滤（这里过滤条件为null），过滤后的field执行callback方法
        ReflectionUtils.filterFieldsInClass(bean.getClass(), null, new Consumer<Field>() {
            @Override
            public void accept(Field field) {
                ResInjection annotation = field.getAnnotation(ResInjection.class);
                if (annotation != null){
                    injectStorage(bean,field,annotation);
                }
            }
        });

        return bean;
    }

    private void injectStorage(Object bean, Field field, ResInjection annotation){
        /*
        getType()：返回一个 Class 对象，它标识了此 Field 对象所表示字段的声明类型。
        getGenericType()：返回一个 Type 对象，它表示此 Field 对象所表示字段的声明类型。
         */
        Type type = field.getGenericType();

        if (!(type instanceof ParameterizedType)){
            throw new RuntimeException(StringUtils.format("[bean:{}]类型声明不正确，不是泛型类", bean.getClass().getSimpleName()));
        }
        //获得父类的泛型参数的实际类型
        Type[] types = ((ParameterizedType)type).getActualTypeArguments();

        //第一个泛型的真实类型
        Class<?> keyClazz  = (Class<?>) types[0];

        Class<?> resourceClazz = (Class<?>) types[1];

        Storage<?, ?> storage = StorageContext.getStorageManager().getStorage(resourceClazz);

        if (storage == null){
            throw new RuntimeException(StringUtils.format("静态类资源[resource:{}]不存在", resourceClazz.getSimpleName()));
        }

        //从一个指定的POJO的Class中获得具有指定注解的Field，只获取子类的Field，不获取父类的Field
        Field[] fields = ReflectionUtils.getFieldsByAnnoInPOJPClass(resourceClazz, Id.class);
        if (fields.length != 1){
            throw new RuntimeException(StringUtils.format("静态类资源[resource:{}]配置没有注解id", resourceClazz.getSimpleName()));
        }
        if (!keyClazz.getSimpleName().toLowerCase().contains(fields[0].getType().getSimpleName().toLowerCase())){
            throw new RuntimeException(StringUtils.format("静态类资源[resource:{}]配置注解[id:{}]类型和泛型类型[type:{}]不匹配"
                    , resourceClazz.getSimpleName(), fields[0].getType().getSimpleName(), keyClazz.getSimpleName()));
        }

        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field,bean,storage);
        storage.setUsable(true);
    }


}

