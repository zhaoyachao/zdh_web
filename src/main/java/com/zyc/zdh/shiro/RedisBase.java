package com.zyc.zdh.shiro;


import com.zyc.zdh.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: RedisBase
 * 
 * @author zyc-admin
 * @date 2018年2月1日
 * @Description: TODO
 */
public class RedisBase {
	@Autowired
	public RedisTemplate<String, Object> redisTemplate;


	/**
	 * 根据key获取数据类型
	 * 
	 * @param key
	 * @return
	 */
	public String type(String key) {
		DataType type = redisTemplate.type(key);
		return type.code();
	}

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final List<String> keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 批量删除key
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 获取所有的key 查询默认的缓存数据库db0
	 * 
	 * @return
	 */
	public Set<String> keys() {
		return redisTemplate.keys("*");
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String key) {
		Object result = null;
		String type = type(key);
		switch (type) {
		case "string":
			ValueOperations<String, Object> operations = redisTemplate
					.opsForValue();
			result = operations.get(key);
			break;
		case "list":
			ListOperations<String, Object> opsForList = redisTemplate
					.opsForList();
			long end = opsForList.size(key);
			result = opsForList.range(key, 0, end);
			break;
		case "set":
			SetOperations<String, Object> opsForSet = redisTemplate.opsForSet();
			result = opsForSet.members(key);
			break;
		case "hash":
			HashOperations<String, Object, Object> opsForHash = redisTemplate
					.opsForHash();
			result = opsForHash.entries(key);
			break;
		default:
			ValueOperations<String, Object> operations1 = redisTemplate
					.opsForValue();
			result = operations1.get(key);
			break;
		}

		return result;
	}

	/**
	 * 根据key获取集合类型 因myRedis~keys
	 * 的集合是redis自己插入的没有使用序列化，所以不能直接使用redisTemplate.opsForZSet()
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> getZset(final String key) {
		Set<byte[]> setb = redisTemplate
				.execute(new RedisCallback<Set<byte[]>>() {

					@Override
					public Set<byte[]> doInRedis(RedisConnection connection)
							throws DataAccessException {
						return connection
								.zRange(getKeySerializable(key), 0, -1);
					}
				});
		Set<String> sets = new HashSet<String>();
		for (byte[] b : setb) {
			sets.add(new String(b));
		}
		return sets;

	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			if (value instanceof List) {
				ListOperations<String, Object> opsForList = redisTemplate
						.opsForList();
				for (Object o : (List) value) {
					opsForList.leftPush(key, value);
				}
				result = true;
			} else if (value instanceof Set) {
				SetOperations<String, Object> opsForSet = redisTemplate
						.opsForSet();
				Iterator iterator = ((Set) value).iterator();
				while (iterator.hasNext()) {
					opsForSet.add(key, iterator.next());
				}
				result = true;
			} else if (value instanceof Map) {
				HashOperations<String, Object, Object> opsForHash = redisTemplate
						.opsForHash();
				opsForHash.putAll(key,
						(Map<? extends Object, ? extends Object>) value);
				result = true;
			} else {
				ValueOperations<String, Object> operations = redisTemplate
						.opsForValue();
				operations.set(key, value);
				result = true;
			}
		} catch (Exception e) {
            LogUtil.error(this.getClass(), e);
		}
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expireTime
	 *            过期时间,默认我们以天为单位
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			if (value instanceof List) {
				ListOperations<String, Object> opsForList = redisTemplate
						.opsForList();
				opsForList.leftPush(key, value);
				result = true;
			} else if (value instanceof Set) {
				ZSetOperations<String, Object> opsForZSet = redisTemplate
						.opsForZSet();
				opsForZSet.add(key, (Set<TypedTuple<Object>>) value);
				result = true;
			} else if (value instanceof Map) {
				HashOperations<String, Object, Object> opsForHash = redisTemplate
						.opsForHash();
				opsForHash.putAll(key,
						(Map<? extends Object, ? extends Object>) value);
				result = true;
			} else {
				ValueOperations<String, Object> operations = redisTemplate
						.opsForValue();
				operations.set(key, value);
				result = true;
			}
			redisTemplate.expire(key, expireTime, TimeUnit.DAYS);
			result = true;
		} catch (Exception e) {
            LogUtil.error(this.getClass(), e);
		}
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param expireTime
	 *            过期时间
	 * @param time
	 *            过期时间单位
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime,
			TimeUnit time) {
		boolean result = false;
		if (time == null) {
			return set(key, value, expireTime);
		}
		try {
			if (value instanceof List) {
				ListOperations<String, Object> opsForList = redisTemplate
						.opsForList();
				opsForList.leftPush(key, value);
				result = true;
			} else if (value instanceof Set) {
				ZSetOperations<String, Object> opsForZSet = redisTemplate
						.opsForZSet();
				opsForZSet.add(key, (Set<TypedTuple<Object>>) value);
				result = true;
			} else if (value instanceof Map) {
				HashOperations<String, Object, Object> opsForHash = redisTemplate
						.opsForHash();
				opsForHash.putAll(key,
						(Map<? extends Object, ? extends Object>) value);
				result = true;
			} else {
				ValueOperations<String, Object> operations = redisTemplate
						.opsForValue();
				operations.set(key, value);
				result = true;
			}
			redisTemplate.expire(key, expireTime, time);
			result = true;
		} catch (Exception e) {
            LogUtil.error(this.getClass(), e);
		}
		return result;
	}

	private byte[] getKeySerializable(String key) {
		@SuppressWarnings("unchecked")
		RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate
				.getKeySerializer();
		return keySerializer.serialize(key);
	}

	private byte[] getValueSerializable(Object value) {
		@SuppressWarnings("unchecked")
		RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate
				.getValueSerializer();
		return valueSerializer.serialize(value);
	}

	/**
	 * 使用redisTemplate.execute()示例
	 */
	public void execute() {
		String v1 = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] value = connection.get(getKeySerializable(""));
				String v = redisTemplate.getStringSerializer().deserialize(
						value);
				return v;
			}

		});
	}
}
