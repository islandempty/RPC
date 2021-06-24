package com.zfoo.protocol.util;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.zfoo.protocol.exception.ExceptionUtils;

/**
 * @author islandempty
 * @since 2021/6/24
 **/
public  abstract class JsonUtils {

    /**
     * 适用于任何场景下的json转换，只要在各个类方法中不调用configure方法，则MAPPER都是线程安全的
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
      * 使用字节码增强技术，只能转换POJO对象
     */
    private static final ObjectMapper MAPPER_TURBO = new ObjectMapper();

    static {
        //序列化
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}

