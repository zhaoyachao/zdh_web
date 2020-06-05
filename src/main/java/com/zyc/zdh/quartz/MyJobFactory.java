package com.zyc.zdh.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName: MyJobFactory
 * 
 * @author zyc-admin
 * @date 2018年3月5日
 * @Description: TODO
 */
@Component("myJobFactory")
public class MyJobFactory extends AdaptableJobFactory  {

	@Autowired
	private AutowireCapableBeanFactory beanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle)
			throws Exception {
		// TODO Auto-generated method stub
		Object jobInstance = super.createJobInstance(bundle);
		//AutowireCapableBeanFactory只是让jobInstance具备了自动配置属性的功能，并不是把jobInstance放入到了spring容器中
		beanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}
