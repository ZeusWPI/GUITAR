package gent.zeus.guitar

import gent.zeus.guitar.mqtt.startMqtt
import kotlinx.coroutines.coroutineScope
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
open class SpringBootApp

suspend fun main(args: Array<String>): Unit = coroutineScope {
    runApplication<SpringBootApp>(*args)

    gent.zeus.guitar.ext.spotify.init()
    gent.zeus.guitar.ext.votes.init()

    startMqtt()
}
