package com.example.springbootkafkaexample.consumer

class LoggingMessageConsumer: MessageConsumer {
    override fun receive(message: String) {
        println("LoggingMessageConsumer received message=[${message}]")
    }
}