package com.zyc.zdh.config;

import com.google.common.collect.Lists;
import com.zyc.zdh.intercepts.RequestLimitInterceptor;
import com.zyc.zdh.intercepts.RequestLoggingInterceptor;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfigure2x implements WebMvcConfigurer {

    public static List<String> excludePath = Lists.newArrayList("/register**","/statics/**","/css/**","/js/**","/fonts/**",
            "/img/**","/smart_doc/**",
            "/plugins/**","/zdh_flow/**","/favicon.ico","/etl/js/**","/etl/css/**",
            "/statics/**","/403","/404","/503","/500","/cron/**","/download/**","/api/**", "/error");
    @Autowired
    private RequestLoggingInterceptor requestLoggingInterceptor;

    @Autowired
    private RequestLimitInterceptor requestLimitInterceptor;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        // viewResolver.setPrefix("/WEB-INF/classes/views/");
        LogUtil.info(this.getClass(), "web.path:" + ConfigUtil.getValue(ConfigUtil.WEB_PATH));
        viewResolver.setPrefix(ConfigUtil.getValue(ConfigUtil.WEB_PATH));
        viewResolver.setSuffix(".html");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(excludePath.toArray(new String[]{}))
                .addResourceLocations(ConfigUtil.getValue(ConfigUtil.WEB_PATH))
                .addResourceLocations(ConfigUtil.getValue(ConfigUtil.WEB_PATH)+"favicon.ico")
                .addResourceLocations(ConfigUtil.getValue(ConfigUtil.WEB_PATH)+"download/")
                .addResourceLocations(ConfigUtil.getValue(ConfigUtil.WEB_PATH)+"css/")
                .addResourceLocations(ConfigUtil.getValue(ConfigUtil.WEB_PATH)+"cron/")
                .addResourceLocations(ConfigUtil.getValue(ConfigUtil.WEB_PATH)+"js/")
                .addResourceLocations(ConfigUtil.getValue(ConfigUtil.WEB_PATH)+"fonts/")
                .addResourceLocations(ConfigUtil.getValue(ConfigUtil.WEB_PATH)+"img/")
                .addResourceLocations(ConfigUtil.getValue(ConfigUtil.WEB_PATH)+"smart_doc/")
                .addResourceLocations(ConfigUtil.getValue(ConfigUtil.WEB_PATH)+"plugins/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        registry.addInterceptor(requestLimitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);

    }
}
