package com.zyc.zdh.config;

import com.zyc.zdh.filter.ZdhFilter;
import com.zyc.zdh.shiro.*;
import com.zyc.zdh.util.Const;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

	@Autowired
	Environment ev;

	@Value("${cookie.path:'/'}")
	private String cookiePath;


	/**
	 * 单独实现shiro 使用的redisTemplate,原因:shiro session 存储到redis时需要使用jdk序列化方式,因此单独新增一个redis序列化方式为jdk
	 *
	 * @param redisConnectionFactory
	 * @return shirRedisTemplate
	 */
	@Bean(name = "shirRedisTemplate")
	public RedisTemplate<String, Object> shirRedisTemplate(
			JedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(redisSerializer);
		JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
		redisTemplate.setHashKeySerializer(redisSerializer);
		redisTemplate.setHashValueSerializer(redisSerializer);
		return redisTemplate;
	}
	
	@Bean(name="shiroRedisCacheManager")
	public ShiroRedisCacheManager shiroRedisCacheManager(RedisTemplate shirRedisTemplate){
		ShiroRedisCacheManager shiroRedisCacheManager=new ShiroRedisCacheManager();
		shiroRedisCacheManager.setRedisTemplate(shirRedisTemplate);
		return shiroRedisCacheManager;
	}


	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
		return lifecycleBeanPostProcessor;
	}

	/**
	 * 4.7.18之后废弃,采用项目本身quartz定时检查session是否过期
	 * @param defaultWebSessionManager
	 * @return
	 */
	@Deprecated
	@Bean(name = "sessionValidationScheduler")
	public ExecutorServiceSessionValidationScheduler getExecutorServiceSessionValidationScheduler(DefaultWebSessionManager defaultWebSessionManager) {
		ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
		scheduler.setInterval(50*1000);
		//scheduler.setSessionManager(defaultWebSessionManager);
		return scheduler;
	}
	
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于md5(md5(""));
		hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
		return hashedCredentialsMatcher;
	}

	@Bean
	public SimpleCredentialsMatcher simpleCredentialsMatcher(){
		SimpleCredentialsMatcher simpleCredentialsMatcher=new MySimpleCredentialsMatcher();
		return simpleCredentialsMatcher;

	}
	
	@Bean(name = "defaultWebSecurityManager")
	public DefaultWebSecurityManager defaultWebSecurityManager(MyRealm myRealm,DefaultWebSessionManager defaultWebSessionManager,ShiroRedisCacheManager shiroRedisCacheManager) {
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(myRealm);
		//defaultWebSecurityManager.setCacheManager(getEhCacheManager());
		defaultWebSecurityManager.setCacheManager(shiroRedisCacheManager);
		defaultWebSecurityManager.setSessionManager(defaultWebSessionManager);
		defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
		//此处带校验--解决UnavailableSecurityManagerException: No SecurityManager accessible to the calling code
		SecurityUtils.setSecurityManager(defaultWebSecurityManager);
		return defaultWebSecurityManager;
	}
	
	@Bean(name = "rememberMeCookie")
	public SimpleCookie rememberMeCookie() {
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// <!-- 记住我cookie生效时间30天 ,单位秒;-->
		simpleCookie.setMaxAge(60*60*24*30);
		simpleCookie.setName("rememberMe");
		return simpleCookie;
	}
	
	/**
	 * cookie管理对象;
	 * 
	 * @return
	 */
	@Bean(name = "rememberMeManager")
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		byte[] cipherKey = Base64.decode("wGiHplamyXlVB11UXWol8g==");
		cookieRememberMeManager.setCipherKey(cipherKey);
		return cookieRememberMeManager;
	}
	

	@Bean
	@DependsOn(value = "lifecycleBeanPostProcessor")
	public MyRealm myRealm(ShiroRedisCacheManager shiroRedisCacheManager) {
		MyRealm myRealm = new MyRealm();
		//启用缓存
		myRealm.setCachingEnabled(true);
		//禁用授权缓存
		myRealm.setAuthorizationCachingEnabled(false);
		myRealm.setAuthorizationCacheName("shiro-AutorizationCache");
		//启用认证信息缓存
		myRealm.setAuthenticationCachingEnabled(true);
		myRealm.setAuthenticationCacheName("shiro-AuthenticationCache");
		myRealm.setCacheManager(shiroRedisCacheManager);
		//认证加密方式,配合FormAuthenticationFilter使用
		//myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		myRealm.setCredentialsMatcher(simpleCredentialsMatcher());
		return myRealm;
	}

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator getAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}
	
	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager defaultWebSecurityManager) {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(defaultWebSecurityManager);
		return aasa;
	}


	@Bean(name = "sessionManager")
	public DefaultWebSessionManager defaultWebSessionManager(SessionDao sessionDao) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(1000*60*60*24*60);
//		//url中是否显示session Id
		sessionManager.setSessionIdUrlRewritingEnabled(false);
//		// 删除失效的session
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(false);
		sessionManager.setSessionValidationInterval(18000000);
		sessionManager.setSessionValidationScheduler(getExecutorServiceSessionValidationScheduler(sessionManager));
		//sessionManager.setSessionValidationScheduler(quartzSessionValidationScheduler2(sessionManager));
		//设置SessionIdCookie 导致认证不成功，不从新设置新的cookie,从sessionManager获取sessionIdCookie
//		sessionManager.setSessionIdCookie(rememberMeCookie());
		sessionManager.getSessionIdCookie().setName("ZDHSESIONID");
		sessionManager.getSessionIdCookie().setPath(cookiePath);
		//sessionManager.getSessionIdCookie().setDomain("");
		sessionManager.getSessionIdCookie().setMaxAge(60*60*24*7);

		sessionManager.setSessionDAO(sessionDao);
		Collection<SessionListener> c=new ArrayList<>();
		c.add(new MyShiroSessionListener());
		sessionManager.setSessionListeners(c);

		return sessionManager;
	}
	
//	@Bean
//	public QuartzSessionValidationScheduler2 quartzSessionValidationScheduler2(DefaultWebSessionManager sessionManager){
//		QuartzSessionValidationScheduler2 quartzSessionValidationScheduler2=new QuartzSessionValidationScheduler2();
//		quartzSessionValidationScheduler2.setSessionManager(sessionManager);
//		quartzSessionValidationScheduler2.setSessionValidationInterval(1000*5);
//		return quartzSessionValidationScheduler2;
//	}
	 @Bean
	 public SessionDao sessionDao(RedisUtil redisUtil,ShiroRedisCacheManager shiroRedisCacheManager) {
	 SessionDao sessionDao = new SessionDao();
	 //设置缓存器的名称
	 sessionDao.setActiveSessionsCacheName(Const.SHIRO_SESSION_CACHE_NAME);
	 //注入缓存管理器默认的是ehcache缓存
	 sessionDao.setCacheManager(shiroRedisCacheManager);
	 //注入缓存管理器2(实现session由redis控制有多种方法，上一步是一种，下面这样写也行)
	 sessionDao.setRedisUtil(redisUtil);
	 return sessionDao;
	 }


	@Bean(name = "filterRegistrationBean1")
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
		proxy.setTargetFilterLifecycle(true);
		proxy.setTargetBeanName("shiroFilter");
		filterRegistrationBean.setFilter(proxy);
		filterRegistrationBean
				.addInitParameter("targetFilterLifecycle", "true");
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/");
		filterRegistrationBean.setOrder(1);
//		filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST,
//				DispatcherType.FORWARD, DispatcherType.INCLUDE,
//				DispatcherType.ERROR);
		return filterRegistrationBean;
	}

	@Bean(name = "zdhfilterRegistrationBean")
	public FilterRegistrationBean zdhFilterRegistrationBean() {
		FilterRegistrationBean bean = new FilterRegistrationBean(zdhFilter());
		bean.setOrder(-2);
		bean.addUrlPatterns("/*");
		return bean;
	}

	@Bean
	public Filter zdhFilter() {
		return new ZdhFilter();
	}


	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(
			@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {

		String project_pre="";
		// SecurityUtils.setSecurityManager(defaultWebSecurityManager);
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setLoginUrl(project_pre+"/login");
		shiroFilterFactoryBean.setSuccessUrl(project_pre+"/index");
		shiroFilterFactoryBean.setUnauthorizedUrl(project_pre+"/404");
		shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
		Map<String, Filter> filterMap1 = shiroFilterFactoryBean.getFilters();
		filterMap1.put("authc", new MyFormAuthenticationFilter());

		shiroFilterFactoryBean.setFilters(filterMap1);
		Map<String, String> filterMap = new LinkedHashMap<String, String>();
		filterMap.put(project_pre+"/static/**", "anon");
		filterMap.put(project_pre+"/js/**", "anon");
		filterMap.put(project_pre+"/css/**", "anon");
		filterMap.put(project_pre+"/img/**", "anon");
		filterMap.put(project_pre+"/api/**", "anon");
		filterMap.put(project_pre+"/logout", "anon");
		filterMap.put(project_pre+"/captcha", "anon");
		filterMap.put(project_pre+"/check_captcha", "anon");
		filterMap.put(project_pre+"/download/**", "anon");
		filterMap.put(project_pre+"/login", "authc");
		filterMap.put(project_pre+"/index", "authc");
		filterMap.put(project_pre+"/register**", "anon");
		filterMap.put(project_pre+"/cron/**", "anon");
		filterMap.put(project_pre+"/register/**", "anon");
		filterMap.put(project_pre+"/retrieve_password", "anon");
		filterMap.put(project_pre+"/404", "anon");
		filterMap.put(project_pre+"/403", "anon");
		filterMap.put(project_pre+"/503", "anon");
		filterMap.put(project_pre+"/500", "anon");
		filterMap.put(project_pre+"/img/favicon**", "anon");
		filterMap.put(project_pre+"/favicon.ico", "anon");
		filterMap.put(project_pre+"/version", "anon");
		filterMap.put(project_pre+"/zdh_download_index", "anon");
		filterMap.put(project_pre+"/zdh_version", "anon");
		filterMap.put(project_pre+"/swagger**", "anon");
		filterMap.put(project_pre+"/api-docs/**", "anon");
		filterMap.put(project_pre+"/get_platform_name", "anon");
		filterMap.put(project_pre+"/get_error_msg", "anon");
		filterMap.put(project_pre+"/smart_doc/**", "anon");

		filterMap.put(project_pre+"/**", "authc");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		return shiroFilterFactoryBean;

	}



}
