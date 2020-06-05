package com.zyc.zdh.cache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.List;

/**
 * @author zyc-admin
 * @data 2018-03-20 10:12
 **/
public class MyCacheManager implements CacheManager{

	private List<String> cacheNames;

	private MyCacheTemplate myCacheTemplate;

	private MyRedisCache myRedisCache;

	public MyRedisCache getMyRedisCache() {
		return myRedisCache;
	}

	public void setMyRedisCache(MyRedisCache myRedisCache) {
		this.myRedisCache = myRedisCache;
	}

	public MyCacheTemplate getMyCacheTemplate() {
		return myCacheTemplate;
	}

	public void setMyCacheTemplate(MyCacheTemplate myCacheTemplate) {
		this.myCacheTemplate = myCacheTemplate;
	}

	public void setCacheNames(List<String> cacheNames) {
		this.cacheNames = cacheNames;
	}

	@Override
	public Cache getCache(String name) {
		//多级缓存实现
		if(name.equals(myCacheTemplate.getName())){
			return myCacheTemplate;
		}
		//redis缓存实现
		if(name.equals(myRedisCache.getName())){
			return myRedisCache;
		}
		return myRedisCache;
	}

	@Override
	public Collection<String> getCacheNames() {

		return cacheNames;
	}
}
