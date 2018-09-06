package com.zyc.zspringboot.disruptor.queue;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.zyc.zspringboot.annotation.MyMark;

/**
 * ClassName: DisruptorQueueStart   
 * @author zyc-admin
 * @date 2018年1月5日  
 * @Description disruptor (功能相当于mq点对点模式)
 */
@MyMark("disruptor point/point")
public class DisruptorQueueStart {

	private int ringBufferSize=1024*4;
	private Disruptor<ParamSupporter> disruptor;
	private static final EventTranslatorOneArg<ParamSupporter, ParamSupporter> eventTranslator=new EventTranslatorOneArg<ParamSupporter, ParamSupporter>() {
		
		@Override
		public void translateTo(ParamSupporter event, long sequence, ParamSupporter arg0) {
			event.setId(arg0.getId());
		}
	};
	public void init(){
		disruptor=new Disruptor<>(new MyEventFactory(), ringBufferSize,new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r);
			}
		});
		disruptor.handleEventsWithWorkerPool(new MyEventHandler(),new MyEventHandler());
		disruptor.start();
	}
	
	public void publishEvent(ParamSupporter paramSupporter){
		disruptor.publishEvent(eventTranslator, paramSupporter);
	}
}
