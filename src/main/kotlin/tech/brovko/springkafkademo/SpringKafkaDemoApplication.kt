package tech.brovko.springkafkademo

import org.springframework.boot.autoconfigure.SpringBootApplication
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import kotlin.concurrent.thread

@SpringBootApplication
class SpringKafkaDemoApplication

fun main(args: Array<String>) {
	//runApplication<SpringKafkaDemoApplication>(*args)
	val topic  = "string-kafka-demo";
	val producer = MyProducer(topic)

	thread {
		(1.. 100).forEach { i ->
			producer.send(i.toString(), "Hello from MyProducer!")
			TimeUnit.SECONDS.sleep(5)
		}
	}

	val consumer = MyConsumer(topic)

	consumer.consume({ record ->
		println("Got key: ${record.key()}, and value ${record.value()}")
	})


	TimeUnit.MINUTES.sleep(5)
	producer.close()
	consumer.close()
}
