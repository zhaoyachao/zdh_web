package com.zyc.zdh.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.Callable;

/**
 * @author zyc-admin
 * @data 2018-03-22 15:13
 **/
public class MyRedisCache implements Cache {

	private static final Logger logger= LoggerFactory.getLogger(MyRedisCache.class);

	private String name;

	private RedisTemplate<String,Object> redisTemplate;

	private RedisCacheManager redisCacheManager;

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public RedisCacheManager getRedisCacheManager() {
		return redisCacheManager;
	}

	public void setRedisCacheManager(RedisCacheManager redisCacheManager) {
		this.redisCacheManager = redisCacheManager;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object getNativeCache() {
		return null;
	}

	@Override
	public ValueWrapper get(Object key) {
		Cache myRedis = redisCacheManager.getCache(getName());
		if(myRedis!=null){
			logger.info("取数据myReids库===key:{}",key);
			if(myRedis.get(key)!=null){
				RedisCacheElement vr=new RedisCacheElement(new RedisCacheKey(key),myRedis.get(key).get());
				return vr;
			}
		}
		return null;
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		return null;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		return null;
	}

	@Override
	public void put(Object key, Object value) {
		Cache myRedis = redisCacheManager.getCache(getName());
		if(myRedis!=null){
			logger.info("插入myReids库===key:{},value:{}",key,value);
			myRedis.put(key,value);
		}
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		return null;
	}

	@Override
	public void evict(Object key) {
		Cache myRedis = redisCacheManager.getCache(getName());
		if(myRedis!=null){
			logger.info("删除myReids库===key:{}",key);
			myRedis.evict(key);
		}
	}

	@Override
	public void clear() {
		Cache myRedis = redisCacheManager.getCache(getName());
		myRedis.clear();
	}
}
