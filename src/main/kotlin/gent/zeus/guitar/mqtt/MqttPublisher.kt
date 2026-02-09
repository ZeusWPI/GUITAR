package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import gent.zeus.guitar.Environment
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage


internal abstract class MqttPublisher(val mqttClient: MqttClient) {
    fun publishObject(obj: Any) {
        val mapper = jacksonObjectMapper()
        val message = MqttMessage(
            mapper.writeValueAsString(obj).toByteArray(),
        ).apply {
            qos = 1
            isRetained = true
        }
        mqttClient.publish(Environment.MQTT_PUBLISH_TOPIC, message)
    }
}
