package gent.zeus.guitar

import kotlin.reflect.KProperty
import kotlin.system.exitProcess

/**
 * singleton object for environment variables
 */
object Environment {

    private val vars = EnvVars()

    val SPOTIFY_CLIENT_ID by vars.Required()
    val SPOTIFY_CLIENT_SECRET by vars.Required()

    val MQTT_HOST by vars.Optional()
    val MQTT_PORT by vars.RequiredBy(::MQTT_HOST)
    val MQTT_LIBRESPOT_LISTEN_TOPIC by vars.RequiredBy(::MQTT_HOST)
    val MQTT_ZODOM_LISTEN_TOPIC by vars.RequiredBy(::MQTT_HOST)
    val MQTT_PUBLISH_TOPIC by vars.RequiredBy(::MQTT_HOST)

    val ZODOM_API_URL by vars.Optional()

    fun load() {
        if (!vars.check()) exitProcess(1)
    }
}

private class EnvVars {
    private val values: MutableMap<String, String> = mutableMapOf()
    private val failures: MutableList<EnvWrapper> = mutableListOf()

    /**
     * check if the environment variables are valid, and prints a log message if it isn't
     *
     * @return whether the environment variables are valid
     */
    fun check(): Boolean {
        if (failures.isEmpty()) return true
        logger.error("following environment variables were required but not set:")
        failures.filterIsInstance<RequiredEnvWrapper>().map { it.failMessage }.forEach { msg ->
            logger.error("    $msg")
        }
        return false
    }

    private fun load(key: String) =
        values[key] ?: (System.getenv(key) ?: "").also { value -> values[key] = value }

    abstract inner class EnvWrapper {
        protected var propertyName = ""

        abstract fun getEnv(key: String): String

        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            propertyName = property.name
            return values[propertyName] ?: ""
        }

        init {
            getEnv()
        }
    }

    private abstract inner class RequiredEnvWrapper : EnvWrapper() {
        abstract val failMessage: String
    }

    inner class Optional : EnvWrapper() {
        override fun getEnv(key: String): String = load(key)
    }

    private inner class Required : RequiredEnvWrapper() {
        override val failMessage = propertyName

        override fun getEnv(key: String): String {
            val value = load(key)
            if (value.isEmpty()) {
                failures.add(this)
                return ""
            }
            return value
        }
    }

    inner class RequiredBy(private val other: KProperty<String>) : RequiredEnvWrapper() {
        override val failMessage: String = "$propertyName (required because ${other.name} is set)"

        override fun getEnv(key: String): String {
            val value = load(key)
            if (!values[other.name].isEmptyOrNull() && value.isEmpty()) {
                failures.add(this)
                return ""
            }
            return value
        }
    }

    private fun String?.isEmptyOrNull(): Boolean = this?.isEmpty() ?: true
}