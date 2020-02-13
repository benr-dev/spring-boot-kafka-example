package com.example.springbootkafkaexample.producer;

interface MessageProducer {
    fun send(message: String)
}
