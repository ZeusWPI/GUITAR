package gent.zeus.guitar

import kotlin.reflect.KProperty
import kotlin.system.exitProcess

private typealias EnvVarProperty = KProperty<String>

/**
 * singleton object for environment variables
 */
object Environment {

    private val vars = EnvVars()

    val SPOTIFY_CLIENT_ID by vars
    val SPOTIFY_CLIENT_SECRET by vars

    val MQTT_HOST by vars
    val MQTT_PORT by vars
    val MQTT_LIBRESPOT_LISTEN_TOPIC by vars
    val MQTT_ZODOM_LISTEN_TOPIC by vars
    val MQTT_PUBLISH_TOPIC by vars

    val ZODOM_API_URL by vars

    private fun getenv(key: String) = System.getenv(key) ?: ""

    private var checkSuccess = true

    fun EnvVarProperty.exists() {
        if (!getenv(this.name).isEmpty()) return
        checkSuccess = false
        logger.error("env variable ${this.name} is required but not set")
    }

    fun List<EnvVarProperty>.exists() = this.forEach { it.exists() }

    fun EnvVarProperty.validate(errorMessage: String, validator: (String) -> Boolean) {
        if (validator(getenv(this.name))) return
        checkSuccess = false
        logger.error("env variable ${this.name} is set to ${getenv(this.name)}: $errorMessage")
    }

    fun List<EnvVarProperty>.validate(errorMessage: String, validator: (String) -> Boolean) =
        this.forEach { it.validate(errorMessage, validator) }

    fun checkEnvVars(exitOnFail: Boolean = false, checkerBlock: Environment.() -> Unit): Boolean {
        this.checkerBlock()
        if (!checkSuccess && exitOnFail) exitProcess(1)
        return checkSuccess
    }
}


private class EnvVars {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = System.getenv(property.name) ?: ""
}
