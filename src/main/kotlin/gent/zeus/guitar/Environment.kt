package gent.zeus.guitar

import kotlin.system.exitProcess

/**
 * singleton object for environment variables
 */
object Environment {
    private var loadFailed = false

    val SPOTIFY_CLIENT_ID = loadRequired("SPOTIFY_CLIENT_ID")
    val SPOTIFY_CLIENT_SECRET = loadRequired("SPOTIFY_CLIENT_SECRET")

    val MQTT_HOST = load("MQTT_HOST".also { notifyDisabled("mqtt", it) })
    val MQTT_PORT = load("MQTT_PORT")
    val MQTT_LIBRESPOT_LISTEN_TOPIC = load("MQTT_LIBRESPOT_LISTEN_TOPIC")
    val MQTT_ZODOM_LISTEN_TOPIC = load("MQTT_ZODOM_LISTEN_TOPIC")
    val MQTT_PUBLISH_TOPIC = load("MQTT_PUBLISH_TOPIC")

    val ZODOM_API_URL = load("ZODOM_API_URL".also { notifyDisabled("zodom data", it) })

    init {
        if (loadFailed) exitProcess(1)
    }

    /**
     * load required environment variable
     *
     * if it is empty/not set, print an error and (indirectly) end the application
     *
     * @return the value of the variable
     */
    private fun loadRequired(key: String): String = System.getenv(key).takeUnless { it?.isEmpty() ?: true } ?: run {
        logger.error("environment variable $key required but not set!")
        loadFailed = true
        return ""
    }

    /**
     * load a non-required environment variable
     *
     * @return the value of the variable, or an empty string if it is not set
     */
    private fun load(key: String): String = System.getenv(key).takeUnless { it?.isEmpty() ?: true } ?: ""

    private fun notifyDisabled(what: String, envVariableKey: String) {
        if (!load(envVariableKey).isEmpty()) return
        logger.warn("$what is disabled because environment variable $envVariableKey is not set")
    }
}
