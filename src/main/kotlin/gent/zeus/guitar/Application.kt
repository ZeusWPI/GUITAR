package gent.zeus.guitar

import gent.zeus.guitar.mqtt.MqttEnv
import gent.zeus.guitar.ext.spotify.SpotifyToken
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.system.exitProcess


@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class DoStartupChecks : CommandLineRunner {
    //TODO despringify
    private val checklist: List<StartupCheck> = listOf(
        SpotifyToken,
        MqttEnv,
    )

    override fun run(vararg args: String?) {
        logger.info("running startup checks...")

        checklist.map { it.checkOnStartup() }.map {
            if (!it.checkPassed) {
                logger.error(it.message)
            }
            it
        }.let { checklist ->
            if (checklist.any { !it.checkPassed }) {
                exitProcess(1)
            }
        }

        logger.info("startup checks complete!")
    }
}
