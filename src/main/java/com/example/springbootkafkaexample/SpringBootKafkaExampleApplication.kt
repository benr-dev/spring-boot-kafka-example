package com.example.springbootkafkaexample

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpringBootKafkaExampleApplication {
}

fun main(args: Array<String>) {
    SpringApplication.run(SpringBootKafkaExampleApplication::class.java, *args)
}