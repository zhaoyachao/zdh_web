package com.zyc.zdh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service("postConstructUtil")
public class PostConstructUtil {

	private static Logger logger= LoggerFactory.getLogger(PostConstructUtil.class);
	public PostConstructUtil(){
		logger.info("自定义前置任务初始化构造器");
	}
	
	@PostConstruct
	public void init(){
		logger.info("自定义前置任务运行init方法");
	}
	
	@PreDestroy
	public void destroy(){
		
	}
}
