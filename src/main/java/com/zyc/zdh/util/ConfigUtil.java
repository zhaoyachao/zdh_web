package com.zyc.zdh.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 当前项目配置参数工具类
 * 单独写此工具的作用: 屏蔽env配置
 *
 * 使用限制：redis, paramutil初始化相关配置不可使用,因当前类使用了redis,paramutil不可循环引用
 */
public class ConfigUtil {

    public static String getValue(String key, String value){
        Environment environment= getEnv();
        return environment.getProperty(key, value);
    }

    public static String getValue(String key){
        Environment environment= getEnv();
        return environment.getProperty(key);
    }

    public static Environment getEnv(){
        Environment environment= (Environment) SpringContext.getBean("environment");
        return environment;
    }

    public static ParamUtil getParamUtil(){
        ParamUtil paramUtil= (ParamUtil) SpringContext.getBean("paramUtil");
        return paramUtil;
    }

    public static boolean isInValue(String key, String value){
        String tmp = getValue(key, "");
        if(StringUtils.isEmpty(tmp)){
            return false;
        }
        return  Arrays.stream(tmp.split(",")).map(str->str.toLowerCase()).collect(Collectors.toSet()).contains(value.toLowerCase());
    }

    public static boolean isInEnv(String key){
        return getEnv().containsProperty(key);
    }

    public static boolean isInRedis(String key){
        return getParamUtil().exists(key);
    }

    public static boolean isInRedisValue(String key, String value){
        String tmp = getParamUtil().getValue(key, "").toString();
        if(StringUtils.isEmpty(tmp)){
            return false;
        }
        return  Arrays.stream(tmp.split(",")).map(str->str.toLowerCase()).collect(Collectors.toSet()).contains(value.toLowerCase());
    }

}
