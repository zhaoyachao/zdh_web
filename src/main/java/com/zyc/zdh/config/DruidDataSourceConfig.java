package com.zyc.zdh.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DruidDataSourceConfig {
	public Logger logger= LoggerFactory.getLogger(this.getClass());
	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.url}")
	private String dbUrl2;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.username}")
	private String username2;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.password}")
	private String password2;

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName2;

	@Value("${spring.datasource.initialSize}")
	private int initialSize;

	@Value("${spring.datasource.minIdle}")
	private int minIdle;

	@Value("${spring.datasource.maxActive}")
	private int maxActive;

	@Value("${spring.datasource.maxWait}")
	private int maxWait;

	@Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;

	@Value("${spring.datasource.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;

	@Value("${spring.datasource.validationQuery}")
	private String validationQuery;

	@Value("${spring.datasource.testWhileIdle}")
	private boolean testWhileIdle;

	@Value("${spring.datasource.testOnBorrow}")
	private boolean testOnBorrow;

	@Value("${spring.datasource.testOnReturn}")
	private boolean testOnReturn;

	@Value("${spring.datasource.poolPreparedStatements}")
	private boolean poolPreparedStatements;

	@Value("${spring.datasource.filters}")
	private String filters;

	@Value("${spring.datasource.logSlowSql}")
	private String logSlowSql;

	@Bean
	@Primary
	public DataSource dataSource() {
		// @Primary 注解作用是当程序选择dataSource时选择被注解的这个
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(dbUrl);
		datasource.setUsername(username);
		datasource.setPassword(password);
		datasource.setDriverClassName(driverClassName);
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
		}
		return datasource;
	}


	@Bean(name = "dataSource2")
	public DataSource dataSource2() {
		// @Primary 注解作用是当程序选择dataSource时选择被注解的这个
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(dbUrl2);
		datasource.setUsername(username2);
		datasource.setPassword(password2);
		datasource.setDriverClassName(driverClassName2);
		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
		}
		return datasource;
	}

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate(){
		JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource2());
		return jdbcTemplate;
	}

	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		reg.addInitParameter("loginUsername", username);
		reg.addInitParameter("loginPassword", password);
		reg.addInitParameter("logSlowSql", logSlowSql);
		return reg;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions",
				"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		filterRegistrationBean.addInitParameter("profileEnable", "true");
		filterRegistrationBean.setOrder(10);
		return filterRegistrationBean;
	}
}
