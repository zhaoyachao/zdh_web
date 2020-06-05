package com.zyc.zdh.publish;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: MyApplicationListener   
 * @author zyc-admin
 * @date 2018年2月7日  
 * @Description: TODO  
 */
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof MyPublishEvent){
			System.out.println(event.getSource());
		}
		
	}

}
