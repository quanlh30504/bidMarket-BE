//package com.example.bidMarket.KafkaService;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//import org.springframework.kafka.core.KafkaTemplate;
//@Configuration
//public class KafkaConfig {
//
//    @Value("${spring.kafka.topic.bid_request}")
//    private String topic_bidRequest;
//
//    @Value("${spring.kafka.partitions.bid}")
//    private int partition_bid;
//    @Bean
//    public NewTopic bidRequest(){
//        return new NewTopic(topic_bidRequest,partition_bid,(short)1);
//    }
//}