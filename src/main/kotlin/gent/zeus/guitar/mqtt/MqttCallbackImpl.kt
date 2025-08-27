package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gent.zeus.guitar.Logging
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.stereotype.Component


@Component
internal class MqttCallbackImpl(val publisher: MqttPublisher) : MqttCallback {

    init {
        Logging.log.info("listening to mqtt on tcp://${MqttEnv.URL}:${MqttEnv.PORT}")
    }

    override fun connectionLost(cause: Throwable?) {
        Logging.log.error("connection to mqtt server lost", cause)
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        message ?: return
        val playingJson: MqttPlayingJson = with(jacksonObjectMapper()) {
            readValue(
                String(message.payload)
            )
        }

        publisher.publishTrackDetails(playingJson.trackId)
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
    }
}
