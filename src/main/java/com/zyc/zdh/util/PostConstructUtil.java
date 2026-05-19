package com.zyc.zdh.util;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service("postConstructUtil")
public class PostConstructUtil {

	public PostConstructUtil(){
		LogUtil.info(this.getClass(), "自定义前置任务初始化构造器");
	}
	
	@PostConstruct
	public void init(){
		LogUtil.info(this.getClass(), "自定义前置任务运行init方法");
	}
	
	@PreDestroy
	public void destroy(){
		
	}
}
