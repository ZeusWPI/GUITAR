package gent.zeus.guitar.mqtt

import gent.zeus.guitar.StartupCheck
import gent.zeus.guitar.StartupCheckResult
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.*


internal object MqttEnv : StartupCheck {
    val URL: String? = System.getenv("MQTT_SERVER_URL")
    val PORT: String? = System.getenv("MQTT_SERVER_PORT")
    val LISTEN_TOPIC: String? = System.getenv("MQTT_LISTEN_TOPIC")
    val PUBLISH_TOPIC: String? = System.getenv("MQTT_PUBLISH_TOPIC")

    override fun checkOnStartup(): StartupCheckResult {
        val passed = with(MqttEnv) {
            URL != null && PORT != null && LISTEN_TOPIC != null && PUBLISH_TOPIC != null
        }

        return StartupCheckResult(
            passed,
            "MQTT_SERVER URL, MQTT_SERVER_PORT and/or MQTT_LISTEN_TOPIC environment variable not set!".takeUnless { passed },
        )
    }
}

private val MQTT_OPTIONS = MqttConnectOptions().apply {
    isAutomaticReconnect = true
    isCleanSession = true
    connectionTimeout = 10
}


@Component
private class MqttClientProvider {

    @Bean
    fun mqttClient(): MqttClient = MqttClient(
        "tcp://${MqttEnv.URL}:${MqttEnv.PORT}",
        "GUITAR-" + UUID.randomUUID().toString(),
        MemoryPersistence(),
    )
}


@Component
class Mqtt : ApplicationRunner {

    @Autowired
    lateinit var mqttCallback: MqttCallback

    @Autowired
    lateinit var mqttClient: MqttClient

    override fun run(args: ApplicationArguments?) {
        mqttClient.setCallback(mqttCallback)
        mqttClient.connect(MQTT_OPTIONS)
        mqttClient.subscribe(MqttEnv.LISTEN_TOPIC)
    }
}
