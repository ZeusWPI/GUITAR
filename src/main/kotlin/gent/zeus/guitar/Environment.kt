package gent.zeus.guitar

import kotlin.reflect.KProperty
import kotlin.system.exitProcess

object Environment {
    //
    // use https://kotlinlang.org/docs/delegated-properties.html#storing-properties-in-a-map
    //

    private var loadFailed = false

    val SPOTIFY_CLIENT_ID = loadRequired("SPOTIFY_CLIENT_ID")
    val SPOTIFY_CLIENT_SECRET = loadRequired("SPOTIFY_CLIENT_SECRET")

    val MQTT_HOST = loadRequired("MQTT_HOST")
    val MQTT_PORT = loadRequired("MQTT_PORT")

    init {
        if (loadFailed) exitProcess(1)
    }

    private fun loadRequired(key: String): String = System.getenv(key).let {
        if (it.isEmpty()) {
            logger.fatal("environment variable $key required but not set!")
            loadFailed = true
            return ""
        }
        return it
    }

    private fun String.requires(key: String, vararg others: String) {
        if (this.isEmpty()) return
        if (others.all { !it.isEmpty() }) return

        logger.fatal("following environment variables are required by $key but are not set:")
        others.filter { it.isEmpty() }.forEach {
            logger.fatal("    ")
        }
    }

    private fun String?.isEmpty() = this == null || this.length == 0
}
