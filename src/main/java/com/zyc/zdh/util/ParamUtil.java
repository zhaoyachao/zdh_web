package com.zyc.zdh.util;

import com.zyc.zdh.shiro.RedisUtil;

/**
 * 参数工具类
 * 单独写此工具的作用: 项目集群部署时,灰度上线,可能存在多个版本参数互斥,因此单独实现根据版本查找参数的工具
 */
public class ParamUtil {

    private String version;

    private RedisUtil redisUtil;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }


    public boolean exists(String key){
        if(redisUtil.exists(getKey(key)) || redisUtil.exists(key)){
            return true;
        }
        return false;
    }


    public Object getValue(String key){
        //根据version_key获取参数,如果不存在则获取key
        if(redisUtil.exists(getKey(key))){
            return redisUtil.get(getKey(key));
        }
        return redisUtil.get(key);
    }

    public Object getValue(String key, String value){
        //根据version_key获取参数,如果不存在则获取key
        if(redisUtil.exists(getKey(key))){
            return redisUtil.get(getKey(key));
        }
        return redisUtil.get(key, value);
    }

    private String getKey(String key){
        return version + "_" + key;
    }


    public void setValue(Object value){

    }
}
