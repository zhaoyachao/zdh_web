package com.zyc.zspringboot.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * ClassName: ShiroRedisCache
 * 
 * @author zyc-admin
 * @date 2018年3月1日
 * @Description: TODO
 */
@SuppressWarnings("unchecked")
public class ShiroRedisCache<K, V> implements Cache<K, V> {

	private String cacheKey;

	
	private RedisTemplate redisTemplate;

	public ShiroRedisCache(String name, RedisTemplate redisTemplate) {
		this.cacheKey="shiro:cache:" + name + ":";
		this.redisTemplate=redisTemplate;
	}

	
	@Override
	public void clear() throws CacheException {
		// TODO Auto-generated method stub
		redisTemplate.delete(keys());
	}

	@Override
	public V get(K arg0) throws CacheException {

		return (V) redisTemplate.opsForValue().get(getCacheKey(arg0));
	}

	@Override
	public Set<K> keys() {

		return redisTemplate.keys(getCacheKey("*"));
	}

	@Override
	public V put(K arg0, V arg1) throws CacheException {
		
		V old = get(arg0);
		redisTemplate.boundValueOps(getCacheKey(arg0)).set(arg1);
		redisTemplate.expire(getCacheKey(arg0), 1, TimeUnit.HOURS);
		return old;
	}

	@Override
	public V remove(K arg0) throws CacheException {
		V old = get(arg0);
		redisTemplate.delete(getCacheKey(arg0));
		return old;
	}

	@Override
	public int size() {
		return keys().size();
	}

	@Override
	public Collection<V> values() {
		Set<K> keys = keys();
		List<V> list = new ArrayList<>();
		for (K s : keys) {
			list.add(get(s));
		}
		return list;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public K getCacheKey(Object arg0) {
		return (K) (cacheKey + arg0);
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
