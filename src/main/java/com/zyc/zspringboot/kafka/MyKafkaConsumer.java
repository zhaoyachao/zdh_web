package com.zyc.zspringboot.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zyc-admin
 * @data 2018-03-29 11:14
 **/
@Component
public class MyKafkaConsumer {

	@Autowired
	private KafkaTemplate<String,String> kafkaTemplate;

	@KafkaListener(topics = {"my-topic"})
	public void consumer(String message){
		System.out.printf("打印message消息{%s}",message);
		System.out.println();
	}

	public void send(String message){
		kafkaTemplate.send("my-topic",String.valueOf(1),message);
	}
}
