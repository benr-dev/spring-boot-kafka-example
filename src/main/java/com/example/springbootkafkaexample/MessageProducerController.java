package com.example.springbootkafkaexample;

import com.example.springbootkafkaexample.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageProducerController {

    @Autowired
    private TopicService topicService;

    @PostMapping("/send/{topicName}")
    public void sendMessage(@PathVariable String topicName, @RequestBody String message) {
        topicService.sendMessage(topicName, message);
    }
}
