package com.example.springbootkafkaexample

import com.example.springbootkafkaexample.consumer.MessageConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import java.util.*

@Configuration
@EnableKafka
class KafkaConsumerConfiguration {

    @Value(value = "\${kafka.bootstrap-address}")
    lateinit var bootstrapAddress: String

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        val configProps = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
        )

        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun messageListener(@Autowired messageConsumer: MessageConsumer): MessageListener<String,String> {
        return MessageListener { data -> messageConsumer.receive(data.value()) }
    }

    // The name of this Bean is important as Spring Kafka autoconfig requires it
    @Bean
    fun kafkaListenerContainerFactory(
            @Autowired messageListener: MessageListener<String,String>,
            @Autowired consumerFactory: ConsumerFactory<String,String>
    ): KafkaMessageListenerContainer<String,String> {
        val containerProperties = ContainerProperties("test")
        containerProperties.messageListener = messageListener
        containerProperties.groupId = "test-group"

        return KafkaMessageListenerContainer(consumerFactory, containerProperties)
    }
}