package com.example.springbootkafkaexample;

import com.example.springbootkafkaexample.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestMessageProducerController {

    @MockBean
    private TopicService topicService;

    @Autowired
    private MessageProducerController uut;

    @Test
    public void when_controllerSendMessage_then_topicServiceInvoked() throws Exception {
        doNothing().when(topicService).sendMessage(anyString(), anyString());
        uut.sendMessage("test", "this is a test");
        verify(topicService, times(1)).sendMessage("test", "this is a test");
    }
}
