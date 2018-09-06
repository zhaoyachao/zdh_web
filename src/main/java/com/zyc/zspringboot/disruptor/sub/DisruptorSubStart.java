package com.zyc.zspringboot.disruptor.sub;

import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.zyc.zspringboot.annotation.MyMark;

/**
 * ClassName: DisruptorStart   
 * @author zyc-admin
 * @date 2018年1月5日  
 * @desc disruptor配置启动类(功能相当于mq pub/sub发布订阅模式)
 */
@MyMark("disruptor pub/sub")
public class DisruptorSubStart {

	private int ringBufferSize=1024*4;
	private Disruptor<ParamSupporter> disruptor;
	private static final EventTranslatorOneArg<ParamSupporter,ParamSupporter> translatorOneArg=new EventTranslatorOneArg<ParamSupporter,ParamSupporter>(){

		@Override
		public void translateTo(ParamSupporter event, long sequence,
				ParamSupporter arg0) {
			//使用深拷贝方式
			event.setId(arg0.getId());
		}
	};
	public void init(){
		disruptor=new Disruptor<>(new MyEventFactory(), ringBufferSize, new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				
				return new Thread(r);
			}
		},ProducerType.MULTI,new YieldingWaitStrategy());
		//每个eventhandler都可以收到
		MyEventHandler handler1=new MyEventHandler();
		MyEventHandler handler2=new MyEventHandler();
		disruptor.handleEventsWith(handler1,handler2);
		//如果handler1指挥需要在跟一个处理可以使用after then
		//disruptor.after(handler1).then(new MyEventHandler());
		disruptor.start();
	}
	
	public void publishEvent(ParamSupporter paramSupporter){
		//第一种方式发布
		disruptor.publishEvent(translatorOneArg, paramSupporter);
		//第二种方式发布
		//disruptor.getRingBuffer().publishEvent(translatorOneArg, paramSupporter);
	}
	
}
