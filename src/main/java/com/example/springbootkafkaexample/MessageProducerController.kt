package com.example.springbootkafkaexample

import com.example.springbootkafkaexample.service.TopicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageProducerController(@Autowired val topicService: TopicService) {

    @PostMapping("/send/{topicName}")
    fun sendMessage(@PathVariable topicName: String?, @RequestBody message: String?) {
        topicService.sendMessage(topicName!!, message!!)
    }
}