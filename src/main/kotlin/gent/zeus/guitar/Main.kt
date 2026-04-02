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

fun isModuleEnabledAndLog(
    moduleName: String,
    envVar: EnvVarProperty,
    warnDisabled: Boolean = false,
    warnEnabled: Boolean = false
): Boolean {
    val enabled = !(System.getenv(envVar.name)?.isEmpty() ?: true)

    val warn = if (enabled) warnEnabled else warnDisabled

    val message = if (enabled)
        "$moduleName is ENABLED (${envVar.name} is set)"
    else
        "$moduleName is DISABLED (${envVar.name} is not set)"

    if (warn)
        logger.warn(message)
    else
        logger.info(message)

    return enabled
}
