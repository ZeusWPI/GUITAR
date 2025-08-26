package gent.zeus.guitar.mqtt

import gent.zeus.guitar.Logging
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttCallbackImpl : MqttCallback {
    override fun connectionLost(cause: Throwable?) {
        Logging.log.error("connection to mqtt server lost", cause)
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        Logging.log.info("message received !!!! hyayy")
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        TODO("Not yet implemented")
    }
}
