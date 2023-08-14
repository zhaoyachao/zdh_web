package com.zyc.zdh.config;

import com.zyc.zdh.quartz.MyJobFactory;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * quartz配置类
 * 
 * @author Administrator
 * 
 */
@Configuration
public class QuartzConfig {

	public Logger logger = LoggerFactory.getLogger(this.getClass());

//	@Value("${spring.datasource.url}")
//	private String dbUrl;
//
//	@Value("${spring.datasource.username}")
//	private String username;
//
//	@Value("${spring.datasource.password}")
//	private String password;
//
//	@Value("${spring.datasource.driver-class-name}")
//	private String driverClassName;
//
//	@Value("${quartz.instancename}")
//	private String quartzInstance;
//
//	@Value("${org.quartz.threadPool.threadCount}")
//	private String threadCount;

	@Autowired
	Environment ev;

	@Autowired
	ConfigurableEnvironment cev;

	@Resource(name = "dataSource2")
	DataSource dataSource2;


	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(MyJobFactory myJobFactory) {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

		schedulerFactoryBean.setDataSource(dataSource2);
		// 使job实例(本文中job实例是MyJobBean)支持spring 容器管理
		schedulerFactoryBean.setOverwriteExistingJobs(true);
		schedulerFactoryBean.setJobFactory(myJobFactory);
		// schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
		// org.springframework.core.io.Resource res=new
		// ClassPathResource("quartz.properties");
		// schedulerFactoryBean.setConfigLocation(res);
		schedulerFactoryBean.setQuartzProperties(quartzProperties(cev));
		boolean autoStartup = ev.getProperty("zdh.schedule.quartz.auto.startup", Boolean.TYPE, false);
		schedulerFactoryBean.setAutoStartup(autoStartup);
		// 延迟25s启动quartz
		schedulerFactoryBean.setStartupDelay(25);

		return schedulerFactoryBean;
	}

	@Bean
	public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();

//		try {
//			//scheduler.start();
//		} catch (SchedulerException e) {
//			// TODO Auto-generated catch block
//			 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}", e.getCause());
//		}
		return scheduler;
	}

	public Properties quartzProperties(ConfigurableEnvironment ev) {
		Properties prop = new Properties();
		Iterator<PropertySource<?>> i = ev.getPropertySources().iterator();
		while (i.hasNext()){
			PropertySource n = i.next();
			if(n.getName().contains("applicationConfig")){
				Map<String,Object> config = (Map<String,Object>)n.getSource();
				for (String key: config.keySet()){
					if(key.contains("zdh.schedule.")){
						String k = key.split("zdh.schedule.")[1];
						prop.put(k, config.get(key));
						logger.info("zdh schedule config "+k+"====>"+config.get(key).toString());
					}
				}
			}
		}


//		prop.put("quartz.scheduler.instanceName", quartzInstance);
//		prop.put("org.quartz.scheduler.instanceId", "AUTO");
//		prop.put("org.quartz.scheduler.instanceIdGenerator.class", "com.zyc.zdh.quartz.ZdhInstanceIdGenerator");
//		prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
//		// prop.put("org.quartz.scheduler.jobFactory.class",
//		// "org.quartz.simpl.SimpleJobFactory");
//		// JobStoreTX
//		// prop.put("org.quartz.jobStore.class",
//		// "org.quartz.impl.jdbcjobstore.JobStoreTX");
//		prop.put("org.quartz.jobStore.class",
//				"org.quartz.impl.jdbcjobstore.JobStoreCMT");
//		prop.put("org.quartz.jobStore.driverDelegateClass",
//				"org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
//		prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");
//		prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
//		prop.put("org.quartz.jobStore.isClustered", "true");
//		//故障检测时间30s
//		prop.put("org.quartz.jobStore.clusterCheckinInterval","30000");
//		prop.put("org.quartz.threadPool.class",
//				"org.quartz.simpl.SimpleThreadPool");
//		prop.put("org.quartz.threadPool.threadCount", threadCount);
//
//		prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
//		prop.put("org.quartz.jobStore.misfireThreshold", "50000");
//		// org.quartz.jobStore.txIsolationLevelSerializable 如果为true 会出现无法连续事物的错误
//		prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "false");
//		prop.put("org.quartz.jobStore.useProperties", "true");
//		//
//		prop.put("org.quartz.jobstore.acquireTriggerWithinLock", "true");
//
//		 prop.put("org.quartz.dataSource.quartzDataSource.driver",
//		 driverClassName);
//		 prop.put("org.quartz.dataSource.quartzDataSource.URL",
//				 dbUrl);
//		 prop.put("org.quartz.dataSource.quartzDataSource.user", username);
//		 prop.put("org.quartz.dataSource.quartzDataSource.password", password);
//		 prop.put("org.quartz.dataSource.quartzDataSource.maxConnections",
//		 "10");
//
//		prop.put("org.quartz.jobStore.dontSetAutoCommitFalse", "false");

		return prop;
	}

}
