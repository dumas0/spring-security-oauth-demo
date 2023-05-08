package com.dumas.sso.oauth.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author dumas
 * @date 2023/4/25
 */
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Json 化
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Json 序列化出错：{}" + obj, e);
            return null;
        }
    }

    /**
     * Json 解析
     *
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            log.error("Json 解析出错：{}" + json, e);
            return null;
        }
    }

    /**
     * 解析 List 的 Json 数据
     *
     * @param json
     * @param eClass
     * @param <E>
     * @return
     */
    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (JsonProcessingException e) {
            log.error("Json 解析出错：{}" + json, e);
            return null;
        }
    }

    /**
     * Json 转 Map
     *
     * @param json
     * @param kClass
     * @param vClass
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (JsonProcessingException e) {
            log.error("Json 解析出错：{}" + json, e);
            return null;
        }
    }

    /**
     * Json 解析自定义类型
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            log.error("Json 解析出错：{}" + json, e);
            return null;
        }
    }
}
