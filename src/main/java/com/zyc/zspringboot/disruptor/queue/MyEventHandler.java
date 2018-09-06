package com.zyc.zspringboot.disruptor.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.WorkHandler;
/**
 * 
 * ClassName: MyEventHandler   
 * @author zyc-admin
 * @date 2018年1月5日  
 * @Description: TODO
 */
public class MyEventHandler implements WorkHandler<ParamSupporter> {

	private static Logger logger=LoggerFactory.getLogger(MyEventHandler.class);
	
	@Override
	public void onEvent(ParamSupporter event) throws Exception {
		System.out.println(this.hashCode()+"==="+event.getId());
		
	}

}