package com.zyc.zdh.publish;

import org.springframework.context.ApplicationEvent;

/**
 * ClassName: MyPublishEvent   
 * @author zyc-admin
 * @date 2018年2月7日  
 * @Description: TODO  
 */
public class MyPublishEvent extends ApplicationEvent {

	public MyPublishEvent(Object source) {
		super(source);
		
	}

}
