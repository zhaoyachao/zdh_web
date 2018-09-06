package com.zyc.zspringboot.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.zyc.zspringboot.quartz.MyJobFactory;

/**
 * quartz配置类
 * 
 * @author Administrator
 * 
 */
@Configuration
public class QuartzConfig {

	@Autowired
	Environment ev;

	@Autowired
	DataSource dataSource;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(MyJobFactory myJobFactory) {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

		schedulerFactoryBean.setDataSource(dataSource);
		// 使job实例(本文中job实例是MyJobBean)支持spring 容器管理
		schedulerFactoryBean.setOverwriteExistingJobs(true);
		schedulerFactoryBean.setJobFactory(myJobFactory);
		// schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
		// org.springframework.core.io.Resource res=new
		// ClassPathResource("quartz.properties");
		// schedulerFactoryBean.setConfigLocation(res);
		schedulerFactoryBean.setQuartzProperties(quartzProperties());
		// 延迟60s启动quartz
		schedulerFactoryBean.setStartupDelay(60);

		return schedulerFactoryBean;
	}

	@Bean
	public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scheduler;
	}

	public Properties quartzProperties() {
		Properties prop = new Properties();
		prop.put("quartz.scheduler.instanceName", "schedulerFactoryBean");
		prop.put("org.quartz.scheduler.instanceId", "AUTO");
		prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
		// prop.put("org.quartz.scheduler.jobFactory.class",
		// "org.quartz.simpl.SimpleJobFactory");
		// JobStoreTX
		// prop.put("org.quartz.jobStore.class",
		// "org.quartz.impl.jdbcjobstore.JobStoreTX");
		prop.put("org.quartz.jobStore.class",
				"org.quartz.impl.jdbcjobstore.JobStoreCMT");
		prop.put("org.quartz.jobStore.driverDelegateClass",
				"org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
		prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");
		prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
		prop.put("org.quartz.jobStore.isClustered", "true");
		prop.put("org.quartz.threadPool.class",
				"org.quartz.simpl.SimpleThreadPool");
		prop.put("org.quartz.threadPool.threadCount", "5");

		prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
		prop.put("org.quartz.jobStore.misfireThreshold", "50000");
		// org.quartz.jobStore.txIsolationLevelSerializable 如果为true 会出现无法连续事物的错误
		prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "false");
		prop.put("org.quartz.jobStore.useProperties", "true");

		// prop.put("org.quartz.dataSource.quartzDataSource.driver",
		// "oracle.jdbc.driver.OracleDriver");
		// prop.put("org.quartz.dataSource.quartzDataSource.URL",
		// "jdbc:oracle:thin:@10.31.2.153/orcl");
		// prop.put("org.quartz.dataSource.quartzDataSource.user", "spcp");
		// prop.put("org.quartz.dataSource.quartzDataSource.password", "spcp");
		// prop.put("org.quartz.dataSource.quartzDataSource.maxConnections",
		// "10");

		prop.put("org.quartz.jobStore.dontSetAutoCommitFalse", "false");

		return prop;
	}

}
