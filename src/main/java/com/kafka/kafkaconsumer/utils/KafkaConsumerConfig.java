//package com.kafka.kafkaconsumer.utils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.CommonLoggingErrorHandler;
//import org.springframework.kafka.listener.ContainerProperties.AckMode;
//
//@EnableKafka
//@Configuration
//public class KafkaConsumerConfig {
//	
//	@Autowired
//	public ConsumerFactory<String, String> consumerFactory() {
//		Map<String, Object> props = new HashMap<>();
//		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.29.137:29092");
//		props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group1");
//		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "50");
//		return new DefaultKafkaConsumerFactory<>(props);
//	}
//
//	@Autowired
//	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
//		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//		factory.setConsumerFactory(consumerFactory());
//		factory.getContainerProperties().setAckMode(AckMode.MANUAL);
//		factory.setBatchListener(true);
//		factory.setCommonErrorHandler(new CommonLoggingErrorHandler());
//		return factory;
//	}
//	
//	
////	@Bean
////	public ConsumerFactory<String, String> roleConsumerFactory() {
////		Map<String, Object> props = new HashMap<>();
////		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localshost:29092");
////		props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group1");
////		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
////		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
////		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "50");
////		return new DefaultKafkaConsumerFactory<>(props);
////	}
//	
////	@Bean
////	public ConsumerFactory<String, String> roleConsumerFactory() {
////		Map<String, Object> props = new HashMap<>();
////		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localshost:29092");
////		props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group1");
////		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
////		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
////		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "50");
////		return new DefaultKafkaConsumerFactory<>(props);
////	}
////
////	@Bean
////	public ConcurrentKafkaListenerContainerFactory<String, String> roleKafkaListenerContainerFactory() {
////		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
////		factory.setConsumerFactory(roleConsumerFactory());
////		factory.getContainerProperties().setAckMode(AckMode.MANUAL);
////		factory.setBatchListener(true);
////		factory.setCommonErrorHandler(new CommonLoggingErrorHandler());
////		
////		return factory;
////	}
//}
