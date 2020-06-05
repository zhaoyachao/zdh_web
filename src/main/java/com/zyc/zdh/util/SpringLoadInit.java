package com.zyc.zdh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.zyc.zdh.annotation.MyMark;
import com.zyc.zdh.netty.tcp.NettyServer;
import com.zyc.zdh.netty.udp.NettyUdpServer;

/**
 * ClassName: SpringLoadInit
 * 
 * @author zyc-admin
 * @date 2017年12月26日
 * @Description: TODO
 */
@Component
public class SpringLoadInit implements
		ApplicationListener<ContextRefreshedEvent>,Ordered {

	private static Logger logger = LoggerFactory
			.getLogger(SpringLoadInit.class);
	private ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		applicationContext = event.getApplicationContext();
		String[] beanNamesForAnnotation = event.getApplicationContext().getBeanNamesForAnnotation(MyMark.class);
	    for(String bean:beanNamesForAnnotation){
	    	//此处实现有标记注解的类中想做的操作
	    	System.out.println(bean);
	    }
	}
	
	private void initSocket(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO tcp服务
				((NettyServer) applicationContext.getBean("nettyServer"))
						.startServer();
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO udp服务
				((NettyUdpServer) applicationContext.getBean("nettyUdpServer"))
						.startServer();
			}
		}).start();

		logger.debug("加载初始化程序完成");
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
