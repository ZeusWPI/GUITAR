package gent.zeus.guitar.mqtt

import gent.zeus.guitar.StartupCheck
import gent.zeus.guitar.StartupCheckResult
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.*


internal object MqttEnv : StartupCheck {
    val URL: String? = System.getenv("MQTT_SERVER_URL")
    val PORT: String? = System.getenv("MQTT_SERVER_PORT")
    val LIBRESPOT_LISTEN_TOPIC: String = System.getenv("MQTT_LIBRESPOT_LISTEN_TOPIC")
    val ZODOM_LISTEN_TOPIC: String = System.getenv("MQTT_ZODOM_LISTEN_TOPIC")
    val PUBLISH_TOPIC: String = System.getenv("MQTT_PUBLISH_TOPIC")

    val hostString: String get() = "tcp://$URL:$PORT"
    val clientId = "GUITAR-" + UUID.randomUUID().toString()

    override fun checkOnStartup(): StartupCheckResult {
        val passed = with(MqttEnv) {
            listOf(URL, PORT, LIBRESPOT_LISTEN_TOPIC, ZODOM_LISTEN_TOPIC, PUBLISH_TOPIC).all { it != null }
        }

        return StartupCheckResult(
            passed,
            "MQTT_SERVER_URL, MQTT_SERVER_PORT, MQTT_PUBLISH_TOPIC and/or MQTT_LISTEN_TOPIC environment variable not set!"
                .takeUnless { passed },
        )
    }
}


//@Component
class Mqtt : ApplicationRunner {

    private val client = MqttCallbackClient()

    override fun run(args: ApplicationArguments?) {
        client.connect(MqttEnv.LIBRESPOT_LISTEN_TOPIC, MqttEnv.ZODOM_LISTEN_TOPIC)
    }
}
