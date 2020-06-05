package com.zyc.zdh.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

@Service("postConstructUtil")
public class PostConstructUtil {

	public PostConstructUtil(){
		System.out.println("初始化构造器");
	}
	
	@PostConstruct
	public void init(){
		System.out.println("运行init方法");
	}
	
	@PreDestroy
	public void destroy(){
		
	}
}
