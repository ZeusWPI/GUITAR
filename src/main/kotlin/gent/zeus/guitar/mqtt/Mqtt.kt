package gent.zeus.guitar.mqtt

import gent.zeus.guitar.StartupCheck
import gent.zeus.guitar.StartupCheckResult
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.UUID


internal object MqttEnv : StartupCheck {
    val URL: String? = System.getenv("MQTT_SERVER_URL")
    val PORT: String? = System.getenv("MQTT_SERVER_PORT")
    val TOPIC: String? = System.getenv("MQTT_LISTEN_TOPIC")

    override fun checkOnStartup(): StartupCheckResult {
        val passed = with(MqttEnv) {
            URL != null && PORT != null && TOPIC != null
        }

        return StartupCheckResult(
            passed,
            "MQTT_SERVER URL, MQTT_SERVER_PORT and/or MQTT_LISTEN_TOPIC environment variable not set!".takeUnless { passed },
        )
    }
}

private val MQTT_CLIENT = MqttClient(
    "tcp://${MqttEnv.URL}:${MqttEnv.PORT}",
    UUID.randomUUID().toString(),
    MemoryPersistence(),
)
private val MQTT_OPTIONS = MqttConnectOptions().apply {
    isAutomaticReconnect = true
    isCleanSession = true
    connectionTimeout = 10
}

@Component
class Mqtt : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        MQTT_CLIENT.setCallback(MqttCallbackImpl())
        MQTT_CLIENT.connect(MQTT_OPTIONS)
        MQTT_CLIENT.subscribe(MqttEnv.TOPIC)

        /*
                MQTT_CLIENT.subscribe("music/guitar/enter") { topic, message ->
                    publishTestMessage(
                        MQTT_CLIENT, topic, MqttMessage(
                            String(message.payload).uppercase().toByteArray()
                        )
                    )
                }
        */
    }

    private fun publishTestMessage(client: MqttClient, topic: String, message: MqttMessage) {
        message.apply {
            isRetained = true
            qos = 1
        }.run {
            client.publish("music/guitar/answer", this)
        }
    }
}
