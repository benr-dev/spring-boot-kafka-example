package com.example.springbootkafkaexample

import com.example.springbootkafkaexample.consumer.LoggingMessageConsumer
import com.example.springbootkafkaexample.consumer.MessageConsumer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessageConsumerConfiguration {

    @Bean
    fun messageConsumer(): MessageConsumer {
        return LoggingMessageConsumer()
    }
}