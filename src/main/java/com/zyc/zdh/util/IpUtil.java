package com.zyc.zdh.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.zyc.zdh.queue.Message;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

public class IpUtil {



    public static DelayQueue<Message> queue = new DelayQueue<Message>();

    public static LoadingCache<String, RateLimiter> ipRequestCaches = CacheBuilder.newBuilder()
            .maximumSize(100000)// 设置缓存个数
            .expireAfterWrite(300, TimeUnit.SECONDS)
            .build(new CacheLoader<String, RateLimiter>() {
                @Override
                public RateLimiter load(String s) throws Exception {
                    return RateLimiter.create(1000);// 新的IP初始化 (限流每秒100个令牌响应)
                }
            });

    public static LoadingCache<String, RateLimiter> userRequestCaches = CacheBuilder.newBuilder()
            .maximumSize(100000)// 设置缓存个数
            .expireAfterWrite(300, TimeUnit.SECONDS)
            .build(new CacheLoader<String, RateLimiter>() {
                @Override
                public RateLimiter load(String s) throws Exception {
                    return RateLimiter.create(1000);// 新的IP初始化 (限流每秒100个令牌响应)
                }
            });


    public static String getCityByIp(String ip){
        try {
            return "内网";
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

}
