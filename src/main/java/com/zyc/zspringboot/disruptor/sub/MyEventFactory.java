package com.zyc.zspringboot.disruptor.sub;

import com.lmax.disruptor.EventFactory;

/**
 * ClassName: MyEventFactory   
 * @author zyc-admin
 * @date 2018年1月5日  
 * @Description: TODO  
 */
class MyEventFactory implements EventFactory<ParamSupporter>{

	@Override
	public ParamSupporter newInstance() {
		// TODO Auto-generated method stub
		return new ParamSupporter();
	}

}
