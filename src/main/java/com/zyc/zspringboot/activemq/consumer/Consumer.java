package com.zyc.zspringboot.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class Consumer {

	private final static Logger logger = LoggerFactory
			.getLogger(Consumer.class);

	@JmsListener(destination = "table.queue", containerFactory = "jmsQueueListener")
	public void receiveQueue(TextMessage text, Session session)
			throws JMSException {
		try {
			System.out.println("Consumer收到的报文为:" + text.getText());
			if (text.getJMSRedelivered()) {// 如果成立表示是重发的消息
				// logErrorService.updateLog(text.getText());
			}
			text.acknowledge();
		} catch (JMSException e1) {
			if (!text.getJMSRedelivered()) {
				// 第一次发送消息失败记录到日志
			}
			session.recover();// 此不可省略 重发信息使用
		}

	}

	@JmsListener(destination = "ActiveMQ.DLQ", containerFactory = "jmsQueueListener")
	public void receiveMessage(final TextMessage text, Session session) {
		try {
			logger.info("收到死信中的消息{}", text.getText());
			text.acknowledge();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
