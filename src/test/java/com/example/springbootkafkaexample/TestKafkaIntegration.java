package com.example.springbootkafkaexample;

import com.example.springbootkafkaexample.consumer.MessageConsumer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;

@EmbeddedKafka(
        partitions = 1,  // keep partitioning simple
        topics = { TestKafkaIntegration.topic },
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
    private MessageProducerController messageProducerController;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @SpyBean // Using this so we can verify the content of the parameter without mocking
    MessageConsumer messageConsumer;

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public static final String topic = "test";

    @BeforeEach
    public void setUp() {
        for(MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(
                    messageListenerContainer,
                    embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    @Test
    public void when_messageSentViaApi_then_messageReceivedViaLocalConsumer() {
        final String message = "this is test 1";

        messageProducerController.sendMessage(topic, message);

        // Create a consumer for the message topic to verify the contents
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, String> consumer = cf.createConsumer();

        // Read the contents of the topic
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, topic);
        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer);
        consumer.unsubscribe();

        assertThat(records.count(), is(1));

        ConsumerRecord<String,String> consumerRecord = records.records(topic).iterator().next();
        assertThat(consumerRecord, hasValue(message));

    }

    @Test
    public void when_messageSentViaEmbedded_then_messageReceivedViaMessageConsumer() {
        final String message = "this is test 2";
        kafkaTemplate.send(topic, message);
        verify(messageConsumer).receive(message);
    }
}
