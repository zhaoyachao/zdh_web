package com.zyc.zdh.util;

import com.zyc.zdh.annotation.MyMark;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

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
		LogUtil.debug(this.getClass(), "加载初始化程序完成");
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
