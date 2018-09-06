package com.zyc.zspringboot.activemq.invoke;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

public class ConnectionMQ {
	static ActiveMQConnectionFactory connectionFactory;
	static Connection connection;
	static Session session;
	static MessageProducer messageProducer;
	static RedeliveryPolicy redeliveryPolicy;

	public static void initMq(String user, String password, String url) {
		try {
			getRedeliveryPolicy();
			connectionFactory = new ActiveMQConnectionFactory(user, password,
					url);
			connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
			connection = connectionFactory.createConnection();

			connection.start();

			session = connection.createSession(false, 4);// 第一个参数代表不开启事物，第二个参数表示消息确认方式
			messageProducer = session.createProducer(null);
			messageProducer.setDeliveryMode(2);// 设置持久化
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	private static RedeliveryPolicy getRedeliveryPolicy() {
		redeliveryPolicy = new RedeliveryPolicy();
		// 是否在每次尝试重新发送失败后,增长这个等待时间
		redeliveryPolicy.setUseExponentialBackOff(true);
		// 重发次数,默认为6次 这里设置为10次
		redeliveryPolicy.setMaximumRedeliveries(10);
		// 重发时间间隔,默认为1秒
		redeliveryPolicy.setInitialRedeliveryDelay(1);
		// 第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,这里的2就是value
		redeliveryPolicy.setBackOffMultiplier(2);
		// 是否避免消息碰撞
		redeliveryPolicy.setUseCollisionAvoidance(false);
		// 设置重发最大拖延时间-1 表示没有拖延只有UseExponentialBackOff(true)为true时生效
		redeliveryPolicy.setMaximumRedeliveryDelay(-1);
		return redeliveryPolicy;
	}

	public static void close() {
		try {
			messageProducer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void sendMessage(String queue, String message) {
		Destination destination;
		try {
			destination = session.createQueue(queue);
			TextMessage text = session.createTextMessage(message);
			messageProducer.send(destination, text);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		initMq("admin", "admin", "tcp://10.31.2.153:61616");
		sendMessage("table.queue", "我给你发送了一条信息" + new Date());
		close();// 关闭资源
	}

}
