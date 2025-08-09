package com.zyc.zdh.config;

import com.zyc.zdh.cache.MyCacheManager;
import com.zyc.zdh.cache.MyCacheTemplate;
import com.zyc.zdh.cache.MyRedisCache;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.ParamUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	private String password;

	private int connectTimeOut;

	private int readTimeOut;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getConnectTimeOut() {
		return connectTimeOut;
	}

	public void setConnectTimeOut(int connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
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

	@Autowired
	public Environment ev;


	@Bean
	public JedisClientConfiguration jedisClientConfiguration() {
		JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
				.connectTimeout(Duration.ofMillis(connectTimeOut)) // 连接超时时间
				.readTimeout(Duration.ofMillis(readTimeOut))    // 读取超时时间
				.usePooling()                          // 使用连接池
				.poolConfig(jedisPoolConfig())                // 设置连接池配置
				.build();
		return jedisClientConfiguration;
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
	public JedisConnectionFactory redisConnectionFactory() {
		// 如果集群使用new JedisConnectionFactory(new
		// RedisClusterConfiguration()),集群配置在RedisClusterConfiguration,这里省略具体配置
		JedisConnectionFactory redisConnectionFactory =null;
		if(hostName.contains(",")){
			//redis 集群模式
			RedisClusterConfiguration rc=new RedisClusterConfiguration();
			for(String hp:hostName.split(",")){
				RedisNode rn=new RedisNode(hp.split(":")[0],Integer.parseInt(hp.split(":")[1]));
				rc.addClusterNode(rn);
			}
			if(!StringUtils.isEmpty(password)){
				rc.setPassword(password);
			}

			redisConnectionFactory=new JedisConnectionFactory(rc,jedisClientConfiguration());
		}else{
			RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName,port);
			if(!StringUtils.isEmpty(password)){
				redisStandaloneConfiguration.setPassword(password);
			}

			redisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration());
		}

		//redisConnectionFactory.setTimeout(timeOut);
		//redisConnectionFactory.setPassword(password);
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
		//JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		redisTemplate.setValueSerializer(redisSerializer);
		redisTemplate.setHashKeySerializer(redisSerializer);
		redisTemplate.setHashValueSerializer(redisSerializer);
		return redisTemplate;
	}

	@Bean
	public RedisUtil redisUtil(RedisTemplate<String,Object> redisTemplate) {
		RedisUtil redisUtil = new RedisUtil();
		redisUtil.setRedisTemplate(redisTemplate);
		return redisUtil;
	}

	@Bean
	public ParamUtil paramUtil(RedisUtil redisUtil){
		ParamUtil paramUtil = new ParamUtil();
		paramUtil.setRedisUtil(redisUtil);
		paramUtil.setVersion(ev.getProperty("version", ""));
		return paramUtil;
	}
	/**
	 * redis缓存管理器
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean("redisCacheManager")
	public RedisCacheManager redisCacheManager(JedisConnectionFactory redisConnectionFactory) {

		Set<String> cacheNames = new HashSet<>();
		cacheNames.add(Const.JAVA_REDISCACHE);
		cacheNames.add(Const.JAVA_2CACHE);
		RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory).initialCacheNames(cacheNames).build();
		//RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		// Number of seconds before expiration. Defaults to unlimited (0)
		//cacheManager.setDefaultExpiration(120); //设置key-value超时时间
		//cacheManager.setCacheNames(cacheNames);
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
		//删除二级ehcache缓存,分布式无法使用单机缓存
		//myCacheTemplate.setEhCacheManager(ehCacheManagerFactoryBean().getObject());
		myCacheTemplate.setRedisCacheManager(redisCacheManager);
		myCacheTemplate.setName(Const.JAVA_2CACHE);
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
		myRedisCache.setName(Const.JAVA_REDISCACHE);
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
		cacheManager.setMyCacheTemplate(myCacheTemplate(redisCacheManager(redisConnectionFactory())));
		cacheManager.setMyRedisCache(myRedisCache(redisCacheManager(redisConnectionFactory()),redisTemplate(redisConnectionFactory())));
		List<String> cacheNames=new ArrayList<>();
		cacheNames.add(Const.JAVA_2CACHE);
		cacheNames.add(Const.JAVA_REDISCACHE);
		cacheManager.setCacheNames(cacheNames);
		return cacheManager;
	}

	 // 整合ehcache
//	 @Bean
//	 public EhCacheCacheManager ehCacheCacheManager() {
//		 EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
//		  return ehCacheCacheManager;
//	 }


//	 @Bean
//	 public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
//	 EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//	 //这里暂时借用shiro的ehcache配置文件
//	 //Resource r=new ClassPathResource("ehcache-shiro.xml");
//	 Resource r=new ClassPathResource(ev.getProperty("ecache.config.location"));
//	 cacheManagerFactoryBean.setConfigLocation(r);
//	 cacheManagerFactoryBean.setShared(true);
//	 return cacheManagerFactoryBean;
//	 }

	@Bean
	public RedissonClient redissonClient(){
		Config config = new Config();
		if(hostName.contains(",")){
			//redis 集群模式
			ClusterServersConfig clusterServersConfig = config.useClusterServers();
			for(String hp:hostName.split(",")){
				clusterServersConfig.addNodeAddress("redis://"+hp.split(":")[0]+":"+hp.split(":")[1]);
			}
			clusterServersConfig.setScanInterval(5000);
			clusterServersConfig.setRetryInterval(500);
			clusterServersConfig.setRetryAttempts(10);
			clusterServersConfig.setPassword(password);
		}else{
			config.useSingleServer().
					setRetryAttempts(10).
					setRetryInterval(500).
					setAddress("redis://"+hostName+":"+port).
					setPassword(password);
		}

		config.setCodec(new StringCodec());
		return Redisson.create(config);
	}
}
