//package com.zyc.zspringboot.kafka;
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.TopicPartition;
//
//import java.util.Properties;
//
///**
// * @author zyc-admin
// * @data 2018-03-14 15:01
// **/
//public class kafkaProduct1 {
//	private final static String URL = "10.31.2.153:2181";
//	private final static String NAME = "test_topic";
//
//
//	/**
//	 * @Description: 生产者
//	 */
//	private static void producer() {
//		Properties props = new Properties();
//		props.put("bootstrap.servers", "10.31.2.153:9092");
//		props.put("acks", "all");
//		props.put("retries", 5);
//		props.put("batch.size", 16384);
//		props.put("linger.ms", 1);
//		props.put("buffer.memory", 33554432);
//		props.put("partitioner.class", "com.zyc.kafka.KafkaCustomPartitioner");
//		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//
//		Producer<String, String> producer = new KafkaProducer<String, String>(props);
//		for (int i = 0; i < 100; i++){
//			System.out.println("发送"+i);
//
//			producer.send(new ProducerRecord<String, String>("my-topic",Integer.toString(i), Integer.toString(i)));
//		}
//
//		producer.close();
//	}
//
//	public static void main(String[] args) {
//		//createTopic();
//		producer();
//	}
//}