package com.example.springbootkafkaexample.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate

@Configuration
class TopicServiceConfiguration(val kafkaTemplate: KafkaTemplate<String, String>) {

    @Bean
    fun topicService(): TopicService {
        return SpringKafkaTopicService(kafkaTemplate)
    }
}