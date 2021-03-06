package com.example.springbootkafkaexample.producer

import com.example.springbootkafkaexample.producer.MessageProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.lang.Nullable
import org.springframework.util.concurrent.ListenableFutureCallback

class SpringKafkaMessageProducer(@param:Autowired val template: KafkaTemplate<String, String>, val topicName: String): MessageProducer {
    override fun send(message: String) {
        val future = template.send(topicName, message)

        future.addCallback(object : ListenableFutureCallback<SendResult<String?, String?>?> {
            override fun onSuccess(@Nullable result: SendResult<String?, String?>?) {
                println("Sent message=[${message}] with offset=[${result!!.recordMetadata.offset()}]")
            }

            override fun onFailure(ex: Throwable) {
                println("Unable to send message=[${message}] due to : ${ex.message}")
            }
        })
    }
}