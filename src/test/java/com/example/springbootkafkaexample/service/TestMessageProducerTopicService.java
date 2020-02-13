package com.example.springbootkafkaexample.service;

import com.example.springbootkafkaexample.producer.MessageProducer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;


public class TestMessageProducerTopicService {

    private MessageProducer messageProducer = mock(MessageProducer.class);
    private Map<String, MessageProducer> producers = Collections.singletonMap("test", messageProducer);
    private MessageProducerTopicService uut = new MessageProducerTopicService(producers);

    @Test
    public void when_topicServiceSendMessage_then_producerIsPassedMessage() throws Exception {
        String topic = "test";
        String message = "this is a test";

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        uut.sendMessage(topic, message);

        verify(messageProducer, times(1)).send(message);
        verify(messageProducer).send(captor.capture());

        String sentMessage = captor.getValue();

        assertThat(sentMessage, is(message));
    }
}
