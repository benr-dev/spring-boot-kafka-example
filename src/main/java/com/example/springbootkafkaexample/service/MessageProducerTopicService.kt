package com.example.springbootkafkaexample.service

import com.example.springbootkafkaexample.producer.MessageProducer

class MessageProducerTopicService(val messageProducerByTopic: Map<String, MessageProducer>) : TopicService {
    override fun sendMessage(topicName: String, message: String) {
        messageProducerByTopic[topicName]?.send(message)
    }

}