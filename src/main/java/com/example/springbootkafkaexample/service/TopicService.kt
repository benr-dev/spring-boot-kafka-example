package com.example.springbootkafkaexample.service

interface TopicService {
    fun sendMessage(topicName: String, message: String)
}
