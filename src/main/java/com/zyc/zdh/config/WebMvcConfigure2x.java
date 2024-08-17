package com.zyc.zdh.config;

import com.zyc.zdh.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
public class WebMvcConfigure2x implements WebMvcConfigurer {

    public Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        // viewResolver.setPrefix("/WEB-INF/classes/views/");
        logger.info("web.path:"+ConfigUtil.getValue("web.path"));
        viewResolver.setPrefix(ConfigUtil.getValue("web.path"));
        viewResolver.setSuffix(".html");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String project_pre="";
        registry.addResourceHandler(project_pre+"/register**",project_pre+"/statics/**",project_pre+"/css/**",project_pre+"/js/**",project_pre+"/fonts/**",
                project_pre+"/img/**",project_pre+"/smart_doc/**",
                project_pre+"/plugins/**",project_pre+"/zdh_flow/**",project_pre+"/favicon.ico",project_pre+"/etl/js/**",project_pre+"/etl/css/**",
                project_pre+"/statics/**",project_pre+"/404**",project_pre+"/cron/**",project_pre+"/download/**")
                .addResourceLocations(ConfigUtil.getValue("web.path"))
                .addResourceLocations(ConfigUtil.getValue("web.path")+"favicon.ico")
                .addResourceLocations(ConfigUtil.getValue("web.path")+"download/")
                .addResourceLocations(ConfigUtil.getValue("web.path")+"css/")
                .addResourceLocations(ConfigUtil.getValue("web.path")+"cron/")
                .addResourceLocations(ConfigUtil.getValue("web.path")+"js/")
                .addResourceLocations(ConfigUtil.getValue("web.path")+"fonts/")
                .addResourceLocations(ConfigUtil.getValue("web.path")+"img/")
                .addResourceLocations(ConfigUtil.getValue("web.path")+"smart_doc/")
                .addResourceLocations(ConfigUtil.getValue("web.path")+"plugins/");
    }
}
