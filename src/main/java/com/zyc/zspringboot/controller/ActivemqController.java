package com.zyc.zspringboot.controller;//package com.zyc.springboot.controller;
//
//import javax.jms.Destination;
//import javax.jms.Queue;
//
//import org.apache.activemq.command.ActiveMQQueue;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.zyc.springboot.activemq.producer.Producer;
//
//@Controller
//public class ActivemqController {
//
//	@Autowired
//	private Producer producer;
//	@Autowired
//	private Queue queue1;
//	
//	
//	@RequestMapping("/sendMessage")
//	@ResponseBody
//	public String sendMessage(@RequestParam("text") String text){
//		producer.sendMessage(queue1, text);
//		return "true";
//	}
//}
