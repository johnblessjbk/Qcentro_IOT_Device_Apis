package com.auth.service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.auth.service.dto.DeviceEvent;


@Configuration
@EnableKafka
public class KafkaConsumerConfig {

 @Value("${spring.kafka.bootstrap-servers}")
 private String bootstrapServers;

 @Bean
 public ConsumerFactory<String, DeviceEvent> consumerFactory() {
     Map<String, Object> props = new HashMap<>();
     props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
     props.put(ConsumerConfig.GROUP_ID_CONFIG, "device-status-updater");
     props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
     props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
     props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.iot.dto");
     props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
     return new DefaultKafkaConsumerFactory<>(props);
 }

 @Bean
 public ConcurrentKafkaListenerContainerFactory<String, DeviceEvent> kafkaListenerContainerFactory() {
     ConcurrentKafkaListenerContainerFactory<String, DeviceEvent> factory = 
         new ConcurrentKafkaListenerContainerFactory<>();
     factory.setConsumerFactory(consumerFactory());
     factory.setConcurrency(3);
     return factory;
 }
}