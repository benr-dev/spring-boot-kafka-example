package com.example.springbootkafkaexample.service;

public interface TopicService {
    public void sendMessage(String topicName, String message);
}
