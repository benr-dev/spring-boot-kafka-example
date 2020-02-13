package com.example.springbootkafkaexample.service

interface TopicService {
    fun sendMessage(topic: String, message: String)
}
