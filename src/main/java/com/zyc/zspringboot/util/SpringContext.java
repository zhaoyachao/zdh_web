package com.zyc.zspringboot.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
@Component
public class SpringContext implements ApplicationContextAware{

	public static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContext.applicationContext=applicationContext;
	}

	public static Object  getBean(String beanName){
		return applicationContext.getBean(beanName);
	}
	
	public static Object getBean(Class<?> c){
		return applicationContext.getBean(c);
	}
	
	public static void publishEvent(ApplicationEvent event){
		applicationContext.publishEvent(event);
	}
}
