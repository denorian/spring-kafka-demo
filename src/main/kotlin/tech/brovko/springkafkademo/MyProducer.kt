package tech.brovko.springkafkademo

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.io.Closeable
import java.util.*

class MyProducer(val topic: String): Closeable {

    private val producer = getProducer()

    fun getProducer(): KafkaProducer<String, String> {
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[ProducerConfig.CLIENT_ID_CONFIG] = "clientId"
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name

        return KafkaProducer<String, String>(props)
    }

    fun send(key: String, value: String) {
        producer
            .send(ProducerRecord(topic, key,value))
            .get()
    }

    override fun close() {
        producer.close()
    }
}