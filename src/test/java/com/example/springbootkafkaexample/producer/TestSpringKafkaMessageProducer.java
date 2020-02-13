package com.example.springbootkafkaexample.producer;

import com.example.springbootkafkaexample.producer.SpringKafkaMessageProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.*;

public class TestSpringKafkaMessageProducer {

    private KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);

    @Test
    public void when_messageProducerSendIsCalled_then_kafkaTemplateInvokedWithTopicAndMessage() throws Exception {
        String topic = "test";
        String message = "this is a test";
        long offset = 1L;
        int partition = 1;

        SpringKafkaMessageProducer uut = new SpringKafkaMessageProducer(kafkaTemplate, topic);

        // Setup mock response data from Kafka
        SendResult<String, Object> sendResult = mock(SendResult.class);
        ListenableFuture<SendResult<String, String>> responseFuture = mock(ListenableFuture.class);
        var recordMetadata = new RecordMetadata(new TopicPartition(topic, partition), offset, 0L, 0L, 0L, 0, 0);
        given(sendResult.getRecordMetadata()).willReturn(recordMetadata);

        // Setup Spring Kafka template to return mock response
        when(kafkaTemplate.send(topic, message)).thenReturn(responseFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback listenableFutureCallback = invocationOnMock.getArgument(0);
            listenableFutureCallback.onSuccess(sendResult);
            assertEquals(sendResult.getRecordMetadata().offset(), offset);
            assertEquals(sendResult.getRecordMetadata().partition(), partition);
            return null;
        }).when(responseFuture).addCallback(any(ListenableFutureCallback.class));

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        uut.send(message);

        verify(kafkaTemplate, times(1)).send(topic, message);
        verify(kafkaTemplate).send(topicCaptor.capture(), messageCaptor.capture());
        assertThat(topicCaptor.getValue(), is(topic));
        assertThat(messageCaptor.getValue(), is(message));
    }
}
