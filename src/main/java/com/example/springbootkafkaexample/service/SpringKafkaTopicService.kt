package com.example.springbootkafkaexample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class SpringKafkaTopicService implements TopicService {
    private final KafkaTemplate<String, String> template;

    public SpringKafkaTopicService(@Autowired KafkaTemplate<String,String> kafkaTemplate) {
        this.template = kafkaTemplate;
    }

    @Override
    public void sendMessage(String topicName, String message) {
        ListenableFuture<SendResult<String, String>> future =
                template.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
