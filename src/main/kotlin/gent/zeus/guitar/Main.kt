package gent.zeus.guitar

import gent.zeus.guitar.mqtt.MqttContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.time.Duration.Companion.seconds


@SpringBootApplication
open class SpringBootApp

suspend fun main(args: Array<String>): Unit = coroutineScope {
    gent.zeus.guitar.ext.spotify.init()
    gent.zeus.guitar.ext.votes.init()

    launch {
        delay(1.seconds)
        MqttContext().startMqtt()
    }

    runApplication<SpringBootApp>(*args)
}
