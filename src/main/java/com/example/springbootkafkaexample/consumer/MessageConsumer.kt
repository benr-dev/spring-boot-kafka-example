package com.example.springbootkafkaexample.consumer;

interface MessageConsumer {
    fun receive(message: String)
}
