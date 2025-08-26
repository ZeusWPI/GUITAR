package gent.zeus.guitar

import gent.zeus.guitar.spotify.SpotifyToken
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import kotlin.system.exitProcess


@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Component
class DoStartupChecks : CommandLineRunner {
    private val checklist: List<StartupCheck> = listOf(
        SpotifyToken
    )

    override fun run(vararg args: String?) {
        Logging.log.info("running startup checks...")

        checklist.asSequence().map { it.checkOnStartup() }.forEach {
            if (!it.checkPassed) {
                Logging.log.error(it.message)
                exitProcess(1)
            }
        }

        Logging.log.info("startup checks complete!")
    }
}
