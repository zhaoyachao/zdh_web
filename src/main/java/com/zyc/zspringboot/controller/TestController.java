package com.zyc.zspringboot.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zyc.zspringboot.annotation.MyMark;
import com.zyc.zspringboot.config.RedisConfig;
import com.zyc.zspringboot.job.MyJobBean;
import com.zyc.zspringboot.util.SpringContext;

/**
 * ClassName: TestController   
 * @author zyc-admin
 * @date 2018年2月7日  
 * @Description: TODO  
 */
@Controller
@RequestMapping("test")
public class TestController {

	@RequestMapping("applicationContext")
	public void testApplicationContext(){
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(req.getServletContext());
		System.out.println(webApplicationContext.getClass());
		for(String s:webApplicationContext.getBeanNamesForAnnotation(MyMark.class)){
			System.out.println(s);
		}
		System.out.println("end-----------");
		
		
	}
	@RequestMapping("quartzJob")
	public void testQuartzJob(){
		RedisConfig redis=SpringContext.applicationContext.getBean(RedisConfig.class);
		System.out.println(redis.getHostName());
	}
}
