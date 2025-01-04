package com.zyc.zdh.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    /**
     * json 数组字符串转java list
     *
     * @param jsonArray     json
     * @param typeReference reference
     * @param <T>           t
     * @return list
     */
    public static <T> T toJavaList(String jsonArray, TypeReference<T> typeReference) {
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(jsonArray, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }


    /**
     * 解析json字符串为Java 对象
     *
     * @param object object
     * @param tClass Bean 类型
     * @param <T>    Class
     * @return java 对象
     */
    public static <T> T toJavaBean(Object object, Class<T> tClass) {
        T t = null;
        try {
            String json = formatJsonString(object);
            t = OBJECT_MAPPER.readValue(json, tClass);
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
}
