//package com.zyc.zdh.config;
//
//import com.zyc.zdh.util.ConfigUtil;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
//import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
//import org.springframework.boot.web.servlet.ErrorPage;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//import org.springframework.web.servlet.view.JstlView;
//
//@Configuration
//@EnableWebMvc
//public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
//	public Logger logger = LoggerFactory.getLogger(this.getClass());
//	@Autowired
//	Environment ev;
////	@Bean
////	public Converter<String, Timestamp> stringToTimeStampConvert() {
////		return new Converter<String, Timestamp>() {
////			@Override
////			public Timestamp convert(String source) {
////				Timestamp date = null;
////				try {
////					date = Timestamp.valueOf(source);
////				} catch (Exception e) {
////					 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}", e.getCause());
////				}
////				return date;
////			}
////		};
////	}
//
//
//	@Override
//	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//		configurer.enable();
//	}
//
//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer() {
//
//		return new EmbeddedServletContainerCustomizer() {
//			@Override
//			public void customize(ConfigurableEmbeddedServletContainer container) {
//				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
//				container.addErrorPages(error404Page);
//			}
//		};
//	}
//
//	@Bean
//	public InternalResourceViewResolver viewResolver() {
//		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//		// viewResolver.setPrefix("/WEB-INF/classes/views/");
//		LogUtil.info(this.getClass(), "web.path:"+ConfigUtil.getValue("web.path"));
//		viewResolver.setPrefix(ConfigUtil.getValue("web.path"));
//		viewResolver.setSuffix(".html");
//		viewResolver.setViewClass(JstlView.class);
//		return viewResolver;
//	}
//
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		//registry.addResourceHandler("/**").addResourceLocations("/");
//		String project_pre="";
//		registry.addResourceHandler(project_pre+"/register**",project_pre+"/statics/**",project_pre+"/css/**",project_pre+"/js/**",project_pre+"/fonts/**",
//				project_pre+"/img/**",project_pre+"/smart_doc/**",
//				project_pre+"/plugins/**",project_pre+"/zdh_flow/**",project_pre+"/favicon.ico",project_pre+"/etl/js/**",project_pre+"/etl/css/**",
//				project_pre+"/statics/**",project_pre+"/404**",project_pre+"/cron/**",project_pre+"/download/**")
//				.addResourceLocations(ConfigUtil.getValue("web.path"))
//				.addResourceLocations(ConfigUtil.getValue("web.path")+"favicon.ico")
//				.addResourceLocations(ConfigUtil.getValue("web.path")+"download/")
//				.addResourceLocations(ConfigUtil.getValue("web.path")+"css/")
//				.addResourceLocations(ConfigUtil.getValue("web.path")+"cron/")
//				.addResourceLocations(ConfigUtil.getValue("web.path")+"js/")
//				.addResourceLocations(ConfigUtil.getValue("web.path")+"fonts/")
//				.addResourceLocations(ConfigUtil.getValue("web.path")+"img/")
//				.addResourceLocations(ConfigUtil.getValue("web.path")+"smart_doc/")
//				.addResourceLocations(ConfigUtil.getValue("web.path")+"plugins/");
//
//	}
//
//}
