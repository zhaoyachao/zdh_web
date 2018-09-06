package com.zyc.zspringboot.activemq.invoke;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public abstract class AbstractConnectionMQ {
	static ConnectionFactory connectionFactory;
	static Connection connection;
	static Session session;
	static MessageProducer messageProducer;
	public static void initMq(String user,String password,String url,String destination) throws JMSException{
		 connectionFactory = new ActiveMQConnectionFactory(user,password,url);
		 connection=connectionFactory.createConnection();
		 connection.start();
		 session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//第一个参数代表不开启事物，第二个参数表示由消息发送者自动发送消息确认
		 Destination queue=session.createQueue(destination);
		 messageProducer=session.createProducer(queue);
		 messageProducer.setDeliveryMode(2);//设置持久化
	}
	public abstract void sendMessage();
}
