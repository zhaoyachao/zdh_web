package com.zyc.zspringboot.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ClassName: TransactionalConfig   
 * @author zyc-admin
 * @date 2018年1月2日  
 * @Description:
 *springboot中使用事物,一般使用默认的即可,<br>
 *添加@EnableTransactionManagement注解,<br>
 *在使用事物的地方添加@Transactional注解即可
 */
@Configuration
@EnableTransactionManagement
public class TransactionalConfig {

	@Bean
	@Primary
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	
	@Bean
	public PlatformTransactionManager txManager2(DataSource dataSource){
		return new DataSourceTransactionManager(dataSource);
	}
}
