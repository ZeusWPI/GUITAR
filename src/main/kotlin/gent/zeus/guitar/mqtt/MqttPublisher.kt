package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage


internal abstract class MqttPublisher(val mqttClient: MqttClient) {
    abstract fun publishTrackDetails(id: String)

    fun publishObject(obj: Any) {
        val mapper = jacksonObjectMapper()
        val message = MqttMessage(
            mapper.writeValueAsString(obj).toByteArray(),
        ).apply {
            qos = 1
            isRetained = true
        }
        mqttClient.publish(MqttEnv.PUBLISH_TOPIC, message)
    }
}
