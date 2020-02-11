package com.example.springbootkafkaexample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class TopicServiceConfiguration {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Bean
    public TopicService topicService(){
        return new SpringKafkaTopicService(kafkaTemplate);
    }
}
