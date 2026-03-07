package com.zyc.zdh.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.zyc.zdh.intercepts.PageInterceptor;
import com.zyc.zdh.util.LogUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * MyBatis 第二数据源配置
 * 用于配置使用 dataSource2 的 Mapper
 * 使用事务时需要指定别名  @Transactional(propagation= Propagation.NESTED, value = "transactionManager2")
 * 如果需要第三个数据源,可直接复制当前类
 */
@Configuration
@MapperScan(
    basePackages = "com.zyc.zdh.dao.secondary",
    sqlSessionFactoryRef = "sqlSessionFactory2",
    sqlSessionTemplateRef = "sqlSessionTemplate2"
)
@EnableTransactionManagement
public class MyBatisSecondaryConfig {

    @Value("${secondary.spring.datasource.name}")
    private String name2;

    @Value("${secondary.spring.datasource.url}")
    private String dbUrl2;

    @Value("${secondary.spring.datasource.username}")
    private String username2;

    @Value("${secondary.spring.datasource.password}")
    private String password2;

    @Value("${secondary.spring.datasource.driver-class-name}")
    private String driverClassName2;

    @Value("${secondary.spring.datasource.initialSize}")
    private int initialSize2;

    @Value("${secondary.spring.datasource.minIdle}")
    private int minIdle2;

    @Value("${secondary.spring.datasource.maxActive}")
    private int maxActive2;

    @Value("${secondary.spring.datasource.maxWait}")
    private int maxWait2;

    @Value("${secondary.spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis2;

    @Value("${secondary.spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis2;

    @Value("${secondary.spring.datasource.validationQuery}")
    private String validationQuery2;

    @Value("${secondary.spring.datasource.testWhileIdle}")
    private boolean testWhileIdle2;

    @Value("${secondary.spring.datasource.testOnBorrow}")
    private boolean testOnBorrow2;

    @Value("${secondary.spring.datasource.testOnReturn}")
    private boolean testOnReturn2;

    @Value("${secondary.spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements2;

    @Value("${secondary.spring.datasource.filters}")
    private String filters2;

    @Value("${secondary.spring.datasource.logSlowSql}")
    private String logSlowSql2;

    @Bean(name = "dataSourceSecondary")
    public DataSource dataSourceSecondary() {
        // @Primary 注解作用是当程序选择dataSource时选择被注解的这个
        DruidDataSource datasource = new DruidDataSource();
        datasource.setName(name2);
        datasource.setUrl(dbUrl2);
        datasource.setUsername(username2);
        datasource.setPassword(password2);
        datasource.setDriverClassName(driverClassName2);
        datasource.setInitialSize(initialSize2);
        datasource.setMinIdle(minIdle2);
        datasource.setMaxActive(maxActive2);
        datasource.setMaxWait(maxWait2);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis2);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis2);
        datasource.setValidationQuery(validationQuery2);
        datasource.setTestWhileIdle(testWhileIdle2);
        datasource.setTestOnBorrow(testOnBorrow2);
        datasource.setTestOnReturn(testOnReturn2);
        datasource.setPoolPreparedStatements(poolPreparedStatements2);
        try {
            datasource.setFilters(filters2);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            LogUtil.error(this.getClass(), e);
        }

        return datasource;
    }

    /**
     * 创建第二数据源的 SqlSessionFactory
     */
    @Bean(name = "sqlSessionFactory2")
    public SqlSessionFactory sqlSessionFactory2(@Qualifier("dataSourceSecondary") javax.sql.DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        
        // 设置Mapper XML文件位置, 此处不设置,采用tk.mapper
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath:mapper/secondary/*.xml"));
        
        // 设置类型别名包
        bean.setTypeAliasesPackage("com.zyc.zdh.entity.secondary");

        return bean.getObject();
    }

    /**
     * 创建第二数据源的 SqlSessionTemplate
     */
    @Bean(name = "sqlSessionTemplate2")
    public SqlSessionTemplate sqlSessionTemplate2(@Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 配置第二数据源的事务管理器
     */
    @Bean(name = "transactionManager2")
    public PlatformTransactionManager transactionManager2(@Qualifier("dataSourceSecondary") javax.sql.DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
