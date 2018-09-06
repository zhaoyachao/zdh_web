package com.zyc.zspringboot.disruptor.sub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;

/**
 * ClassName: MyEventHandler   
 * @author zyc-admin
 * @date 2018年1月5日  
 * @Description	具体实现业务的handler
 */
public class MyEventHandler implements EventHandler<ParamSupporter> {

	private static Logger logger=LoggerFactory.getLogger(MyEventHandler.class);
	@Override
	public void onEvent(ParamSupporter event, long sequence, boolean endOfBatch)
			throws Exception {
		logger.debug("业务流程处理类{}",event.getId());
		Thread.sleep(1000);
		System.out.println(Thread.currentThread().getName()+"业务流程处理类"+event.getId());
	}

}
