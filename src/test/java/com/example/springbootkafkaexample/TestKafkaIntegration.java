package com.example.springbootkafkaexample;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@EmbeddedKafka(
        partitions = 1,  // keep partitioning simple
        topics = {TestKafkaIntegration.topic}, // required to be able to post test message
        brokerProperties={
                "log.dir=build/tmp/embedded-kafka"
        }
)
@SpringBootTest(
        // Using @SpringBootTest for config properties binding, bootstrap processing
        // and autowiring.

        // tell Spring Boot Kafka auto-config about the embedded kafka endpoints
        properties = "kafka.bootstrap-address=${spring.embedded.kafka.brokers}"
)
public class TestKafkaIntegration {

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    MessageProducerController messageProducerController;

    public static final String topic = "test";
    public static final String message = "this is a test";

    @Test
    public void when_messageSentViaApi_then_kafkaReceivesMessage() {
        messageProducerController.sendMessage(topic, message);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, String> consumer = cf.createConsumer();

        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, topic);
        ConsumerRecords<String, String> replies = KafkaTestUtils.getRecords(consumer);

        assertThat(replies.count(), is(1));

        ConsumerRecord<String,String> consumerRecord = replies.records(topic).iterator().next();
        assertThat(consumerRecord.topic(), is(topic));
        assertThat(consumerRecord.value(), is(message));
    }
}
