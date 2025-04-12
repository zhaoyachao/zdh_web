package com.zyc.zdh.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

class TimestampJsonDeserializer extends JsonDeserializer<Timestamp> {
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TimestampJsonDeserializer() {
    }

    @Override
    public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = jp.getText();
        return Timestamp.valueOf(LocalDateTime.parse(date, PATTERN));
    }
}

class DateJsonDeserializer extends JsonDeserializer<Date> {
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DateJsonDeserializer() {
    }

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = jp.getText();
        LocalDate localDate = LocalDate.parse(date, PATTERN);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

class TimestampJsonSerializer extends JsonSerializer<Timestamp> {
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TimestampJsonSerializer() {
    }

    @Override
    public void serialize(Timestamp value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(value.toLocalDateTime().format(PATTERN));
    }
}

class DateJsonSerializer extends JsonSerializer<Date> {
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DateJsonSerializer() {
    }

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        LocalDateTime localDate = value.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        jgen.writeString(PATTERN.format(localDate));
    }
}

public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Timestamp.class, new TimestampJsonSerializer());
        module.addDeserializer(Timestamp.class, new TimestampJsonDeserializer());
        OBJECT_MAPPER.registerModule(module);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false); // 忽略Map中的null值
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);//支持解析单引号
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);//类中无属性禁止抛异常

    }
    /**
     * json 数组字符串转java list
     *
     * @param json     json
     * @param typeReference reference
     * @param <T>           t
     * @return list
     */
    public static <T> T toJavaObj(String json, TypeReference<T> typeReference) {
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            LogUtil.error(JsonUtil.class, e);
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
            LogUtil.error(JsonUtil.class, e);
        }
        return t;
    }

    public static <T> List<T> toJavaListBean(String jsonArray, Class<T> tClass) {
        List<T> t = new ArrayList<>();
        if(StringUtils.isEmpty(jsonArray)){
            return t;
        }
        JavaType type = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, tClass);
        try {
            t = OBJECT_MAPPER.readValue(jsonArray, type);
        } catch (JsonProcessingException e) {
            LogUtil.error(JsonUtil.class, e);
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
            t = OBJECT_MAPPER.readValue(jsonArray, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (JsonProcessingException e) {
            LogUtil.error(JsonUtil.class, e);
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
            LogUtil.error(JsonUtil.class, e);
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
            LogUtil.error(JsonUtil.class, e);
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
            LogUtil.error(JsonUtil.class, e);
        }
        return null;
    }


    public static List<Map<String, Object>> createEmptyListMap(){
        return new ArrayList<>();
    }

    public static Map<String, Object> createEmptyMap(){
        return new LinkedHashMap<String, Object>();
    }

    public static boolean isJsonValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
