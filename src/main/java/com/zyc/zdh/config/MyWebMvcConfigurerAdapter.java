package com.zyc.zdh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
@EnableWebMvc
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

	@Autowired
	Environment ev;
//	@Bean
//	public Converter<String, Timestamp> stringToTimeStampConvert() {
//		return new Converter<String, Timestamp>() {
//			@Override
//			public Timestamp convert(String source) {
//				Timestamp date = null;
//				try {
//					date = Timestamp.valueOf(source);
//				} catch (Exception e) {
//					 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}", e.getCause());
//				}
//				return date;
//			}
//		};
//	}


	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
				container.addErrorPages(error404Page);
			}
		};
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		// viewResolver.setPrefix("/WEB-INF/classes/views/");
        System.out.println("打印web.path:"+ev.getProperty("web.path"));
		viewResolver.setPrefix(ev.getProperty("web.path"));
		viewResolver.setSuffix(".html");
		viewResolver.setViewClass(JstlView.class);
		return viewResolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/**").addResourceLocations("/");
		String project_pre="";
		registry.addResourceHandler(project_pre+"/register**",project_pre+"/statics/**",project_pre+"/css/**",project_pre+"/js/**",project_pre+"/fonts/**",
				project_pre+"/img/**",
				project_pre+"/plugins/**",project_pre+"/zdh_flow/**",project_pre+"/favicon**",project_pre+"/etl/js/**",project_pre+"/etl/css/**",
				project_pre+"/statics/**",project_pre+"/404**",project_pre+"/cron/**",project_pre+"/download/**")
				.addResourceLocations(ev.getProperty("web.path"))
				.addResourceLocations("/statics/")
				.addResourceLocations(ev.getProperty("web.path")+"download/")
				.addResourceLocations(ev.getProperty("web.path")+"css/")
				.addResourceLocations(ev.getProperty("web.path")+"cron/")
				.addResourceLocations(ev.getProperty("web.path")+"js/")
				.addResourceLocations(ev.getProperty("web.path")+"fonts/")
				.addResourceLocations(ev.getProperty("web.path")+"img/")
				.addResourceLocations(ev.getProperty("web.path")+"plugins/")
				.addResourceLocations(ev.getProperty("web.path")+"zdh_flow/")
				.addResourceLocations(ev.getProperty("web.path")+"statics/");

	}

}
