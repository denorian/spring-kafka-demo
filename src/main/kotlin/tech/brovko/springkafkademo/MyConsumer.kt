package tech.brovko.springkafkademo


import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import java.io.Closeable
import java.time.Duration
import java.util.*
import java.util.function.Consumer
import kotlin.concurrent.thread

class MyConsumer(private val topic: String): Closeable {
    private val consumer = getConsumer()

    private fun getConsumer():KafkaConsumer<String, String> {
        val props = Properties()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[ConsumerConfig.GROUP_ID_CONFIG] = "groupId"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name


        val consumer = KafkaConsumer<String, String>(props)
        consumer.subscribe(listOf(topic))

        return consumer
    }

    fun consume(recordConsumer: Consumer<ConsumerRecord<String, String>>){
        thread {
            while (true) {
                val records = consumer.poll(Duration.ofSeconds(1))

                records.forEach {  record ->
                    recordConsumer.accept(record)
                }
            }
        }
    }

    override fun close() {
        TODO("Not yet implemented")
    }


}