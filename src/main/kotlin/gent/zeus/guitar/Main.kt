package gent.zeus.guitar

import gent.zeus.guitar.mqtt.MqttContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.time.Duration.Companion.seconds
import org.slf4j.LoggerFactory


@SpringBootApplication
open class SpringBootApp

suspend fun main(args: Array<String>): Unit = coroutineScope {
    Environment.load()

    launch {
        delay(1.seconds)
        MqttContext().startMqtt()
    }

    runApplication<SpringBootApp>(*args)
}
