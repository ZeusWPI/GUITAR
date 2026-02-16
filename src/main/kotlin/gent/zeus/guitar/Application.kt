package gent.zeus.guitar

import gent.zeus.guitar.mqtt.MqttContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.time.Duration.Companion.seconds


@SpringBootApplication
open class Application

suspend fun main(args: Array<String>): Unit = coroutineScope {
    Environment.SPOTIFY_CLIENT_ID  // access variable to make singleton object

    launch {
        delay(1.seconds)
        MqttContext().startMqtt()
    }

    runApplication<Application>(*args)
}
