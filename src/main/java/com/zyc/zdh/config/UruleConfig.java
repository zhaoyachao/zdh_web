//package com.zyc.zdh.config;
//
//import com.bstek.urule.console.servlet.URuleServlet;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.ImportResource;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//
//import javax.sql.DataSource;
//
//@Configuration
//@ImportResource({"classpath:urule-console-context.xml"})
//@PropertySource(value = {"classpath:urule-console-context.properties"})
//public class UruleConfig {
//    @Bean
//    public PropertySourcesPlaceholderConfigurer propertySourceLoader() {
//        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
//        configurer.setIgnoreUnresolvablePlaceholders(true);
//        configurer.setOrder(1);
//        return configurer;
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource datasource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean
//    public ServletRegistrationBean registerURuleServlet(){
//        return new ServletRegistrationBean(new URuleServlet(),"/urule/*");
//    }
//}