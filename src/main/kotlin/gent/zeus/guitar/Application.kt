package gent.zeus.guitar

import gent.zeus.guitar.ext.spotify.SpotifyToken
import gent.zeus.guitar.mqtt.MqttEnv
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.config.Configurator
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
    // TODO eliminate startup checks (use global Environment object/class)
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

fun quicksort(list: MutableList<Int>): MutableList<Int> = when {
    list.size <= 1 -> list
    else -> list.removeAt(0).let { pivot ->
        quicksort(list.filter { it < pivot }.toMutableList()) +
                pivot +
                quicksort(list.filter { it > pivot }.toMutableList())
    }.toMutableList()
}