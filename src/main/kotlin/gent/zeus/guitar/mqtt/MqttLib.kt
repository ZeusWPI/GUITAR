package gent.zeus.guitar.mqtt

import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedContext
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import gent.zeus.guitar.logException
import gent.zeus.guitar.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * represents a connection to an mqtt broker
 *
 * @param serverHost hostname/ip of server
 * @param serverPort port of broker
 */
class MqttClient(val serverHost: String, val serverPort: Int) {
    private val identifier = "guitar-" + UUID.randomUUID()

    val client: Mqtt5BlockingClient = Mqtt5Client.builder()
        .identifier(identifier)
        .serverHost(serverHost)
        .serverPort(serverPort)
        .automaticReconnectWithDefaultConfig()
        .addConnectedListener(this::connectedListener)
        .buildBlocking()

    private fun connectedListener(ctx: MqttClientConnectedContext) {
        logger.info("mqtt connected to ${ctx.clientConfig.serverHost}:${ctx.clientConfig.serverPort}")
    }

    init {
        logException("failed to connect to mqtt server $serverHost:$serverPort!") {
            client.connect()
        }
    }
}

/**
 * uses an [MqttClient] to listen for messages on specified topics
 *
 * @param client the [MqttClient] to use
 * @param topics topics to subscribe to
 */
class MqttListener(client: MqttClient, vararg topics: String) {
    val publishes = client.client.publishes(MqttGlobalPublishFilter.ALL)

    init {
        topics.forEach { topic ->
            logException("failed to subscribe to topic $topic") {
                client.client.subscribeWith().topicFilter(topic).send()
            }
            logger.info("subscribed to topic $topic")
        }
    }

    /**
     * start listening for messages and launch the callback in a new coroutine when a message is received
     *
     * @param coroutineScope coroutine scope to launch the callback in
     * @param callback function to launch
     */
    fun startListening(coroutineScope: CoroutineScope, callback: suspend (Mqtt5Publish) -> Unit) {
        while (true) {
            val publish = publishes.receive()
            coroutineScope.launch { callback(publish) }
        }
    }
}

/**
 * uses an [MqttClient] to publish messages
 *
 * @param client the [MqttClient] to use
 * @param qos the QOS to use
 * @param retain whether to retain messages
 */
class MqttPublisher(val client: MqttClient, val qos: MqttQos, val retain: Boolean) {
    /**
     * publish a message
     *
     * @param topic topic to publish to
     * @param message content of message
     */
    fun publish(topic: String, message: String) = logException("error publishing message!") {
        val publish = Mqtt5Publish.builder()
            .topic(topic)
            .payload(message.toByteArray(Charsets.UTF_8))
            .qos(qos)
            .retain(retain)
            .build()
        client.client.publish(publish)
    }
}
