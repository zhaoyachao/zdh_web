//package com.zyc.zspringboot.kafka;
//
//import kafka.cluster.Cluster;
//import kafka.utils.VerifiableProperties;
//import org.apache.kafka.clients.producer.Partitioner;
//
//
//import java.util.Map;
//
//public class KafkaCustomPartitioner implements Partitioner {
//	public void configure(Map<String, ?> arg0) {}
//
//	public KafkaCustomPartitioner(){
//
//	}
//	public KafkaCustomPartitioner(VerifiableProperties properties){
//
//	}
//	public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, org.apache.kafka.common.Cluster cluster) {
//		//判断topic中是否包含acpdr
//		return Integer.valueOf(new String(bytes))%6;
//
//	}
//
//	public void close() {}
//
//}