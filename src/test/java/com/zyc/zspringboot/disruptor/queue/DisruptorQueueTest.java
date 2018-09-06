package com.zyc.zspringboot.disruptor.queue;

import java.util.UUID;

import com.zyc.zspringboot.disruptor.queue.DisruptorQueueStart;
import com.zyc.zspringboot.disruptor.queue.ParamSupporter;

public class DisruptorQueueTest {

	public static void main(String[] args) {
		DisruptorQueueStart dis=new DisruptorQueueStart();
		dis.init();
		ParamSupporter paramSupporter =new ParamSupporter();
		for(int i=0;i<100;i++){
			paramSupporter.setId(i+"");
			dis.publishEvent(paramSupporter);
			System.out.println("输出i值"+i);
		}
		
	}

}