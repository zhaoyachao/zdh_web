package com.zyc.zdh.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
    /**
     * json 数组字符串转java list
     *
     * @param jsonArray     json
     * @param typeReference reference
     * @param <T>           t
     * @return list
     */
    public static <T> T toJavaListMap(String jsonArray, TypeReference<T> typeReference) {
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(jsonArray, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * json 数组字符串转java list<map>
     * @param jsonArray
     * @return
     */
    public static List<Object> toJavaList(String jsonArray) {
        List<Object> t = new ArrayList();
        if(StringUtils.isEmpty(jsonArray)){
            return t;
        }
        try {
            t = OBJECT_MAPPER.readValue(jsonArray, List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * json 数组字符串转java list<map>
     * @param jsonArray
     * @return
     */
    public static List<Map<String, Object>> toJavaListMap(String jsonArray) {
        List<Map<String, Object>> t = new ArrayList<>();
        if(StringUtils.isEmpty(jsonArray)){
            return t;
        }
        try {
            t = OBJECT_MAPPER.readValue(jsonArray, List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 解析json字符串为Java 对象
     *
     * @param json string
     * @param tClass Bean 类型
     * @param <T>    Class
     * @return java 对象
     */
    public static <T> T toJavaBean(String json, Class<T> tClass) {
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 解析json字符串为Java 对象
     *
     * @param json string
     * @return java 对象
     */
    public static Map<String, Object> toJavaMap(String json) {
        Map<String, Object> t = new LinkedHashMap<>();
        if(StringUtils.isEmpty(json)){
            return t;
        }
        try {
            t = OBJECT_MAPPER.readValue(json, LinkedHashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将Object对象转化为json string
     *
     * @param o 任意对象
     * @return 字符串
     */
    public static String formatJsonString(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Map<String, Object>> createEmptyListMap(){
        return new ArrayList<>();
    }

    public static Map<String, Object> createEmptyLinkMap(){
        return new LinkedHashMap<String, Object>();
    }
}
