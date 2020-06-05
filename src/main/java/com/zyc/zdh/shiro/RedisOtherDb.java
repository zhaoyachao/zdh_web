package com.zyc.zdh.shiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.stereotype.Service;

/**
 * ClassName: RedisOtherDb
 * 
 * @author zyc-admin
 * @date 2018年2月1日
 * @Description: TODO
 */
@Service
public class RedisOtherDb extends RedisBase {

	/**
	 * 指定缓存库索引,查找key对应的数据类型
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
	 * 根据缓存库批量删除key
	 * 
	 * @param keys
	 * @param dbIndex
	 */
	public void remove(final Collection<String> keys, final int dbIndex) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 根据缓存库索引,删除key对应的数据
	 * 
	 * @param key
	 * @param dbIndex
	 */
	public void remove(final String key, final int dbIndex) {
		if (exists(key, dbIndex)) {
			redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.select(dbIndex);
					return connection.del(getKeySerializable(key));
				}
			});
		}
	}

	/**
	 * 根据缓存库索引，模糊匹配表达式批量删除keys
	 * 
	 * @param pattern
	 * @param dbIndex
	 */
	public void removePattern(final String pattern, final int dbIndex) {
		Set<String> keys = redisTemplate
				.execute(new RedisCallback<Set<String>>() {

					@Override
					public Set<String> doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.select(dbIndex);
						Set<String> set = new HashSet<>();
						for (byte[] b : connection
								.keys(getKeySerializable(pattern))) {
							set.add(new String(b));
						}
						return set;
					}
				});

		if (keys.size() > 0)
			remove(keys, dbIndex);
	}

	/**
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public boolean exists(final String key, final int dbIndex) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(dbIndex);
				return connection.exists(getKeySerializable(key));
			}
		});
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String key, final int dbIndex) {
		Object result = null;
		String type = type(key);
		switch (type) {
		case "string":
			result = redisTemplate.execute(new RedisCallback<String>() {
				@Override
				public String doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.select(dbIndex);
					return (String) redisTemplate.getValueSerializer()
							.deserialize(
									connection.get(getKeySerializable(key)));
				}
			});
			break;
		case "list":
			result = redisTemplate.execute(new RedisCallback<List<Object>>() {
				@Override
				public List<Object> doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.select(dbIndex);
					long end = connection.lLen(getKeySerializable(key));
					List<byte[]> list = connection.lRange(
							getKeySerializable(key), 0, end);
					return (List<Object>) SerializationUtils.deserialize(list,
							redisTemplate.getValueSerializer());
				}
			});
			break;
		case "set":
			result = redisTemplate.execute(new RedisCallback<Set<Object>>() {
				@Override
				public Set<Object> doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.select(dbIndex);
					Set<byte[]> set = connection
							.sMembers(getKeySerializable(key));
					return (Set<Object>) SerializationUtils.deserialize(set,
							redisTemplate.getValueSerializer());
				}
			});
			break;
		case "hash":
			result = redisTemplate.execute(new RedisCallback<Map>() {
				@Override
				public Map doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.select(dbIndex);
					Map<byte[], byte[]> map = connection
							.hGetAll(getKeySerializable(key));
					Map<String, Object> maps = new LinkedHashMap<>(map.size());
					for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
						maps.put((String) redisTemplate.getKeySerializer()
								.deserialize(entry.getKey()),
								(Object) redisTemplate.getValueSerializer()
										.deserialize(entry.getValue()));
					}
					return maps;
				}
			});
			break;
		default:
			result = redisTemplate.execute(new RedisCallback<String>() {
				@Override
				public String doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.select(dbIndex);
					return (String) redisTemplate.getValueSerializer()
							.deserialize(
									connection.get(getKeySerializable(key)));
				}
			});
			break;
		}

		return result;
	}

	/**
	 * 根据缓存数据库索引,插入数据
	 * @param key
	 * @param value
	 * @param dbIndex
	 */
	public void set(final String key, final Object value, final int dbIndex) {
		redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(dbIndex);
				if (value instanceof List) {
					for (Object o : (List) value) {
						connection.lPush(getKeySerializable(key),
								getValueSerializable(o));
					}
				} else if (value instanceof Set) {
					for (Object o : (Set) value) {
						connection.sAdd(getKeySerializable(key),
								getValueSerializable(o));
					}
				} else if (value instanceof Map) {
					final Map<byte[], byte[]> hashes = new LinkedHashMap<byte[], byte[]>(
							((Map) value).size());
					for (Map.Entry<? extends String, ? extends Object> entry : ((Map<String, Object>) value)
							.entrySet()) {
						hashes.put(getKeySerializable(entry.getKey()),
								getValueSerializable(entry.getValue()));
					}
					connection.hMSet(getKeySerializable(key), hashes);
				} else {
					connection.set(getKeySerializable(key),
							getValueSerializable(value));
				}
				return null;
			}
		});
	}

	/**
	 * 根据缓存数据库索引,插入数据,并指定过期时间
	 * @param key
	 * @param value
	 * @param dbIndex
	 * @param expireTime 默认单位是天
	 */
	public void set(final String key, final Object value, final int dbIndex,final long expireTime) {
		redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(dbIndex);
				if (value instanceof List) {
					for (Object o : (List) value) {
						connection.lPush(getKeySerializable(key),
								getValueSerializable(o));
					}
				} else if (value instanceof Set) {
					for (Object o : (Set) value) {
						connection.sAdd(getKeySerializable(key),
								getValueSerializable(o));
					}
				} else if (value instanceof Map) {
					final Map<byte[], byte[]> hashes = new LinkedHashMap<byte[], byte[]>(
							((Map) value).size());
					for (Map.Entry<? extends String, ? extends Object> entry : ((Map<String, Object>) value)
							.entrySet()) {
						hashes.put(getKeySerializable(entry.getKey()),
								getValueSerializable(entry.getValue()));
					}
					connection.hMSet(getKeySerializable(key), hashes);
				} else {
					connection.set(getKeySerializable(key),
							getValueSerializable(value));
				}
				connection.expire(getKeySerializable(key), TimeoutUtils.toSeconds(expireTime,
						TimeUnit.DAYS));
				return null;
			}
		});
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
