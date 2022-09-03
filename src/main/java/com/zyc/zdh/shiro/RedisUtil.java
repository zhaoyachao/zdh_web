package com.zyc.zdh.shiro;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author: zhaoyachao
 * @date:2017年6月30日 上午9:26:55
 * @description:
 * @version :
 * 
 */

public class RedisUtil {

	private Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	private RedisTemplate<String, Object> redisTemplate;

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
	 * 根据key 和缓存库索引 获取数据类型
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public String type(final String key, final int dbIndex) {
		DataType type = redisTemplate.execute(new RedisCallback<DataType>() {

			@Override
			public DataType doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(dbIndex);
				return connection.type(getKeySerializable(key));
			}
		});
		return type.code();
	}

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
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
	 * 删除指定索引数据库中的指定键对应的value
	 * 
	 * @param key
	 *            键
	 * @param dbIndex
	 *            缓存数据库索引
	 */
	public void remove(final String key, final int dbIndex) {
		redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(dbIndex);
				if (connection.exists(redisTemplate.getStringSerializer()
						.serialize(key))) {
					connection.del(redisTemplate.getStringSerializer()
							.serialize(key));
				}
				return true;
			}
		});
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

	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * 根据缓存数据库索引,获取所有的key
	 * 
	 * @param dbIndex
	 * @return
	 */
	public Set<String> keys(final int dbIndex) {
		return redisTemplate.execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(dbIndex);
				Set<byte[]> keys = connection.keys(redisTemplate
						.getStringSerializer().serialize("*"));
				Set<String> keySet = new HashSet<>();
				for (byte[] b : keys) {
					keySet.add(Arrays.toString(b));
				}
				return keySet;
			}
		});

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
	 * 根据key 获取缓存,增加默认值实现
	 * @param key
	 * @param default_value
	 * @return
	 */
	public Object get(final String key, final String default_value) {
		Object value = get(key);
		if(value==null){
			return default_value;
		}
		return value;
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
	 * 指定数据库读取缓存
	 * 
	 * @param key
	 *            键
	 * @param dbIndex
	 *            读取数据库索引
	 * @return
	 */
	public Object get(final String key, final int dbIndex) {
		String v1 = redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(dbIndex);
				byte[] value = connection.get(redisTemplate
						.getStringSerializer().serialize(key));
				String v = redisTemplate.getStringSerializer().deserialize(
						value);
				return v;
			}

		});

		return v1;
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
			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
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
	 * @param dbIndex
	 *            写入数据库索引
	 * @return
	 */
	public boolean set(final String key, final Object value, final int dbIndex) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {

				connection.select(dbIndex);
				connection.set(getKeySerializable(key),
						getValueSerializable(value));
				return true;
			}
		});

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
			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
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
	 * @param dbIndex
	 *            写入库索引
	 * @param expireTime
	 *            过期时间,默认我们以天为单位
	 * @return
	 */
	public boolean set(final String key, final Object value, final int dbIndex,
			final Long expireTime) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {

				connection.select(dbIndex);
				connection.set(getKeySerializable(key),
						getValueSerializable(value));
				connection.expire(redisTemplate.getStringSerializer()
						.serialize(key), TimeoutUtils.toSeconds(expireTime,
						TimeUnit.DAYS));
				return true;
			}
		});

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
			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
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
	 * @param dbIndex
	 *            写入库的索引
	 * @param expireTime
	 *            过期时间
	 * @param time
	 *            过期单位
	 * @return
	 */
	public boolean set(final String key, final Object value, final int dbIndex,
			final Long expireTime, final TimeUnit time) {
		if (time == null) {
			return set(key, value, dbIndex, expireTime);
		}
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {

				connection.select(dbIndex);
				connection.set(getKeySerializable(key),
						getValueSerializable(value));
				connection.expire(redisTemplate.getStringSerializer()
						.serialize(key), TimeoutUtils.toSeconds(expireTime,
						time));
				return true;

			}
		});
		return result;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
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
