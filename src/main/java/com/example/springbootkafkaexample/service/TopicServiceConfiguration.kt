package com.example.springbootkafkaexample.service

import com.example.springbootkafkaexample.producer.SpringKafkaMessageProducer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate

@Configuration
class TopicServiceConfiguration(val kafkaTemplate: KafkaTemplate<String, String>) {

    @Bean
    fun springKafkaMessageProducer(): SpringKafkaMessageProducer {
        return SpringKafkaMessageProducer(kafkaTemplate, "test")
    }

    @Bean
    fun topicService(): TopicService {
        return MessageProducerTopicService(mapOf("test" to springKafkaMessageProducer()))
    }
}