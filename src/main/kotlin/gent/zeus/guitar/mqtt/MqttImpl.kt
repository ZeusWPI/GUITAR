package gent.zeus.guitar.mqtt

import com.hivemq.client.mqtt.datatypes.MqttQos
import gent.zeus.guitar.Environment
import gent.zeus.guitar.logger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

suspend fun startMqtt() = coroutineScope {
    if (Environment.MQTT_HOST.isEmpty()) return@coroutineScope

    val mqttClient = MqttClient(
        Environment.MQTT_HOST,
        Environment.MQTT_PORT.toInt(),  // TODO: exception handling for port.toInt
    )
    val mqttPublisher = MqttPublisher(mqttClient, MqttQos.AT_MOST_ONCE, true)
    MqttListener(mqttClient, "guitartest_in").startListening(this) { msg ->
        val message = msg.payloadAsBytes.decodeToString()
        logger.info("received message: $message on topic: ${msg.topic}")
        logger.info("doing a long calculation...")
        (1..10).forEach {
            delay(1.seconds)
            logger.info("calculation $it/10")
        }
        mqttPublisher.publish("guitartest_out", "calculation done for $message!")
    }
}