package com.zyc.zdh.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.concurrent.Callable;

/**
 * @author zyc-admin
 * @data 2018-03-19 17:15
 **/
public class MyCacheTemplate implements Cache {

	private static final Logger logger = LoggerFactory.getLogger(MyCacheTemplate.class);

	private CacheManager ehCacheManager;

	private RedisCacheManager redisCacheManager;


	public CacheManager getEhCacheManager() {
		return ehCacheManager;
	}

	public void setEhCacheManager(CacheManager ehCacheManager) {
		this.ehCacheManager = ehCacheManager;
	}

	public RedisCacheManager getRedisCacheManager() {
		return redisCacheManager;
	}

	public void setRedisCacheManager(RedisCacheManager redisCacheManager) {
		this.redisCacheManager = redisCacheManager;
	}

	private String name;

	@Override
	public String getName() {
		return name;
	}

	//自己添加set方法,实现Cache本身无此方法
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object getNativeCache() {
		System.out.println("getNativeCache()===========");
		return null;
	}

	@Override
	public ValueWrapper get(Object key) {
		//ehCacheManager=CacheManager.getCacheManager("ec");
		if (ehCacheManager != null) {
			net.sf.ehcache.Cache myEhcache = ehCacheManager.getCache(getName());
			logger.info("取数据ehcache库===key:{}", key);
			if (myEhcache.get(key) != null) {
				ValueWrapper v = new SimpleValueWrapper(myEhcache.get(key).getObjectValue());
				return v;
			}
		}
		Cache myRedis = redisCacheManager.getCache(getName());
		if (myRedis != null) {
			logger.info("取数据reids库===key:{}", key);
			if (myRedis.get(key) != null) {
				RedisCacheElement vr = new RedisCacheElement(new RedisCacheKey(key), myRedis.get(key).get());
				return vr;
			}
		}
		return null;
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		System.out.println(key + "=======================" + type);
		return null;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		return null;
	}

	@Override
	public void put(Object key, Object value) {
		//ehCacheManager=CacheManager.getCacheManager("ec");
		if (ehCacheManager != null) {
			net.sf.ehcache.Cache myEhcache = ehCacheManager.getCache(getName());
			Element e = new Element(key, value);
			logger.info("插入ehcache库===key:{},value:{}", key, value);
			myEhcache.put(e);
		}
		Cache myRedis = redisCacheManager.getCache(getName());
		if (myRedis != null) {
			logger.info("插入reids库===key:{},value:{}", key, value);
			myRedis.put(key, value);
		}
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		return null;
	}

	@Override
	public void evict(Object key) {
		Cache myRedis = redisCacheManager.getCache(getName());
		if (myRedis != null) {
			logger.info("删除reids库===key:{}", key);
			myRedis.evict(key);
		}
		//ehCacheManager=CacheManager.getCacheManager("ec");
		if (ehCacheManager != null) {
			net.sf.ehcache.Cache myEhcache = ehCacheManager.getCache(getName());
			logger.info("删除ehcache库===key:{}", key);
			if (myEhcache.isKeyInCache(key)) {
				myEhcache.remove(key);
			}
		}
	}

	@Override
	public void clear() {
		Cache myRedis = redisCacheManager.getCache(getName());
		myRedis.clear();
		//ehCacheManager=CacheManager.getCacheManager("ec");
		if (ehCacheManager != null) {
			net.sf.ehcache.Cache myEhcache = ehCacheManager.getCache(getName());
			myEhcache.removeAll();
		}
	}
}
