//package com.zyc.zspringboot.kafka;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.TopicPartition;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Properties;
//
///**
// * @author zyc-admin
// * @data 2018-03-14 15:09
// **/
//public class KafkaConsumer1 {
//	private final static String URL = "10.31.2.153:2181";
//
//	/**
//	 * @Description: 消费者
//	 */
//	private static void customer() throws Exception {
//
//		Properties props = new Properties();
//		props.put("bootstrap.servers", "10.31.2.153:9092");
//		props.put("group.id", "test1");
//		props.put("enable.auto.commit", "false");
//		props.put("auto.commit.interval.ms", "1000");
//		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//		props.put("partitioner.class", "com.zyc.kafka.KafkaCustomPartitioner");
//		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//		consumer.subscribe(Arrays.asList("my-topic", "bar"));
//
//
//		while (true) {
//
//			ConsumerRecords<String, String> records = consumer.poll(10000);
//			int count = records.count();
//			System.out.println(count);
//			for ( TopicPartition partition :records.partitions()){
//				List<ConsumerRecord<String, String>> records1 = records.records(partition);
//				System.out.println("partition==="+partition.partition());
//				for (ConsumerRecord<String, String> record : records1){
//					System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
////					if(Integer.valueOf(record.key())%2==0){
////						consumer.commitSync();
////					}
////					throw new Exception();
//				}
//				consumer.commitSync();
//			}
//
//
//		}
//	}
//
//	public static void main(String[] args) throws Exception {
//		customer();
//
//	}
//}
