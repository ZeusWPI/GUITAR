package gent.zeus.guitar.mqtt

import gent.zeus.guitar.Environment
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import java.util.*


internal object MqttEnv {
    val hostString: String get() = "tcp://${Environment.MQTT_HOST}:${Environment.MQTT_PORT}"
    val clientId = "GUITAR-" + UUID.randomUUID().toString()
}


//@Component
class Mqtt : ApplicationRunner {

    private val client = MqttCallbackClient()

    override fun run(args: ApplicationArguments?) {
        client.connect(Environment.MQTT_LIBRESPOT_LISTEN_TOPIC, Environment.MQTT_ZODOM_LISTEN_TOPIC)
    }
}
