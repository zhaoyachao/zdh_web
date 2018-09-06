package com.zyc.zspringboot.config;

import com.zyc.zspringboot.cache.MyCacheManager;
import com.zyc.zspringboot.cache.MyCacheTemplate;
import com.zyc.zspringboot.cache.MyRedisCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: RedisConfig
 *
 * @author zyc-admin
 * @date 2018年1月23日
 * @Description:
 */
@Configuration
@EnableCaching(mode = AdviceMode.PROXY)
// model属性默认proxy
// mode属性，可以选择值proxy和aspectj。默认使用proxy。当mode为proxy时，
// 只有缓存方法在外部被调用的时候才会生效。这也就意味着如果一个缓存方法在一个对
// 象的内部被调用SpringCache是不会发生作用的。而mode为aspectj时，就不会有
// 这种问题了。另外使用proxy的时候，只有public方法上的@Cacheable才会发生作用。
// 如果想非public上的方法也可以使用那么就把mode改成aspectj。
@ConfigurationProperties(prefix = "spring.redis")
// 使用@ConfigurationProperties 需要实现属性的getter setter方法，
// 1.5之前版本需要在启动类上使用@EnableConfigurationProperties进行激活，1.5之后直接在配置类上使用@Component，
// 由于本类使用@Configuration注解包含了@Component就不用在声明@Component，
// 这样就可以直接在其他类中使用@Autowired直接把此类注入进来
//
public class RedisConfig extends CachingConfigurerSupport {

	private String hostName;

	private int port;

	private int timeOut;

	private int maxIdle;// 最大空闲连接数, 默认8个

	private int maxWaitMillis;// 获取连接时的最大等待毫秒数

	private boolean testOnBorrow;// 在获取连接的时候检查有效性, 默认false

	private boolean testWhileIdle;// 空闲是否检查是否有效，默认为false

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}


	@Bean("jedisPoolConfig")
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setTestWhileIdle(false);
		return jedisPoolConfig;
	}

	@Bean
	public JedisConnectionFactory redisConnectionFactory(
			JedisPoolConfig jedisPoolConfig) {
		// 如果集群使用new JedisConnectionFactory(new
		// RedisClusterConfiguration()),集群配置在RedisClusterConfiguration,这里省略具体配置
		JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
		redisConnectionFactory.setPoolConfig(jedisPoolConfig);

		redisConnectionFactory.setHostName(hostName);
		redisConnectionFactory.setPort(port);
		redisConnectionFactory.setTimeout(timeOut);
		return redisConnectionFactory;
	}

	/**
	 * RedisTemplate配置
	 *
	 * @param redisConnectionFactory
	 * @return RedisTemplate
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(
			JedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(redisSerializer);
		// Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new
		// Jackson2JsonRedisSerializer<Object>(
		// Object.class);
		// ObjectMapper om = new ObjectMapper();
		// om.setVisibility(PropertyAccessor.ALL,
		// JsonAutoDetect.Visibility.ANY);
		// om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		// jackson2JsonRedisSerializer.setObjectMapper(om);
		// redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
		return redisTemplate;
	}

	/**
	 * redis缓存管理器
	 * @param redisTemplate
	 * @return
	 */
	@Bean("redisCacheManager")
	public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		// Number of seconds before expiration. Defaults to unlimited (0)
		cacheManager.setDefaultExpiration(120); //设置key-value超时时间
		List<String> cacheNames = new ArrayList<>();
		cacheNames.add("myRedis");
		cacheNames.add("j2CacheRedis");
		cacheManager.setCacheNames(cacheNames);
		return cacheManager;
	}

	/**
	 * spring cache整合(EhCache,Redis)二级缓存具体Cache
	 * @param redisCacheManager
	 * @return
	 */
	@Bean
	@Primary
	public MyCacheTemplate myCacheTemplate(RedisCacheManager redisCacheManager){
		MyCacheTemplate myCacheTemplate=new MyCacheTemplate();
		myCacheTemplate.setEhCacheManager(ehCacheManagerFactoryBean().getObject());
		myCacheTemplate.setRedisCacheManager(redisCacheManager);
		myCacheTemplate.setName("j2CacheRedis");
		return myCacheTemplate;
	}

	/**
	 * 自定义redis缓存
	 * @param redisCacheManager
	 * @param redisTemplate
	 * @return
	 */
	@Bean
	public MyRedisCache myRedisCache(RedisCacheManager redisCacheManager,RedisTemplate<String,Object> redisTemplate){
		MyRedisCache myRedisCache=new MyRedisCache();
		//自定义属性配置缓存名称
		myRedisCache.setName("myRedis");
		//redis缓存管理器
		myRedisCache.setRedisCacheManager(redisCacheManager);
		//redisTemplate 实例
		myRedisCache.setRedisTemplate(redisTemplate);
		return myRedisCache;
	}

	/**
	 * spring cache 统一缓存管理器
	 * @return
	 */
	@Bean("cacheManager")
	@Primary
	@Override
	public CacheManager cacheManager(){
		MyCacheManager cacheManager=new MyCacheManager();
		cacheManager.setMyCacheTemplate(myCacheTemplate(redisCacheManager(redisTemplate(redisConnectionFactory(jedisPoolConfig())))));
		cacheManager.setMyRedisCache(myRedisCache(redisCacheManager(redisTemplate(redisConnectionFactory(jedisPoolConfig()))),redisTemplate(redisConnectionFactory(jedisPoolConfig()))));
		List<String> cacheNames=new ArrayList<>();
		cacheNames.add("j2CacheRedis");
		cacheNames.add("myRedis");
		cacheManager.setCacheNames(cacheNames);
		return cacheManager;
	}

	 // 整合ehcache
	 @Bean
	 public EhCacheCacheManager ehCacheCacheManager() {
		 EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
		  return ehCacheCacheManager;
	 }


	 @Bean
	 public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
	 EhCacheManagerFactoryBean cacheManagerFactoryBean = new
	 EhCacheManagerFactoryBean();
	 //这里暂时借用shiro的ehcache配置文件
	 Resource r=new ClassPathResource("ehcache-shiro.xml");
	 cacheManagerFactoryBean.setConfigLocation(r);
	 cacheManagerFactoryBean.setShared(true);
	 return cacheManagerFactoryBean;
	 }

}
