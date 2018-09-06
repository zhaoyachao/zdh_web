package com.zyc.zspringboot.disruptor.sub;

import java.util.UUID;

import com.zyc.zspringboot.disruptor.sub.DisruptorSubStart;
import com.zyc.zspringboot.disruptor.sub.ParamSupporter;

/**
 * ClassName: DisruptorSubTest   
 * @author zyc-admin
 * @date 2018年1月5日  
 * @Description: TODO  
 */
public class DisruptorSubTest {

	public static void main(String[] args) {
		DisruptorSubStart dis=new DisruptorSubStart();
		dis.init();
		ParamSupporter paramSupporter =new ParamSupporter();
		for(int i=0;i<100;i++){
			paramSupporter.setId(UUID.randomUUID().toString());
			dis.publishEvent(paramSupporter);
			System.out.println("输出i值"+i);
		}
		
	}

}
