package com.zyc.zdh.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * ClassName: ShiroRedisCacheManager   
 * @author zyc-admin
 * @date 2018年3月1日  
 * @Description: TODO  
 */
public class ShiroRedisCacheManager implements CacheManager {

	
    private RedisTemplate<String, Object> redisTemplate;
	
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public <K, V> Cache<K, V> getCache(String arg0) throws CacheException {
		// TODO Auto-generated method stub
		return new ShiroRedisCache<>(arg0,redisTemplate);
	}

}
