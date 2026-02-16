package gent.zeus.guitar.mqtt

import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.datatypes.MqttTopic
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedContext
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import gent.zeus.guitar.logExceptionFail
import gent.zeus.guitar.logExceptionWarn
import gent.zeus.guitar.logger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*

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

    init {
        logger.info("connecting to mqtt...")
        logExceptionFail("failed to connect to mqtt server $serverHost:$serverPort!") {
            client.connect()
        }
    }

    private fun connectedListener(ctx: MqttClientConnectedContext) {
        logger.info("mqtt connected to ${ctx.clientConfig.serverHost}:${ctx.clientConfig.serverPort}")
    }
}

/**
 * uses an [MqttClient] to listen for messages on specified topics
 *
 * @param client the [MqttClient] to use
 */
class MqttListener(private val client: MqttClient) {
    val publishes = client.client.publishes(MqttGlobalPublishFilter.ALL)

    /**
     * you'd think that using a `MutableMap` would be the obvious choice here. but there's no way to get a string
     * representation from an [MqttTopic][com.hivemq.client.mqtt.datatypes.MqttTopic] and it doesn't implement
     * [Object.equals], only [java.lang.Comparable]. so the only way to find the correct entry would be to call
     * `topic.compareTo(otherTopic)` on every item individually, so we might as well just use an array.
     */
    private val callbacks: MutableList<Pair<String, suspend (String) -> Unit>> = ArrayList()

    /**
     * add a callback for messages with the specified topic. this function will be executed (concurrently)
     * when a message is received with that topic.
     *
     * @param topic the topic to listen for
     * @param callback the callback function: takes the message content as argument
     */
    fun addCallback(topic: String, callback: suspend (String) -> Unit) {
        callbacks.add(topic to callback)
        logExceptionFail("failed to subscribe to topic $topic") {
            client.client.subscribeWith().topicFilter(topic).send()
        }
        logger.info("subscribed to topic $topic")
    }

    /**
     * start listening for messages and launch the associated callback in a new coroutine when a message is received.
     * this is a blocking method (runs forever).
     */
    suspend fun startListening() = coroutineScope {
//        launch {
        while (true) {
            val (receivedTopic, content) = with(publishes.receive()) {
                this.topic to this.payloadAsBytes.decodeToString()
            }

            callbacks.forEach { (topic, callback) ->
                if (topic.isTopic(receivedTopic)) this.launch { callback(content) }
            }
        }
//        }
    }

    private fun String.isTopic(topic: MqttTopic): Boolean {
        val thisTopic = MqttTopic.of(this)
        return thisTopic.compareTo(topic) == 0
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
    fun publish(topic: String, message: String) = logExceptionWarn("error publishing message!") {
        val publish = Mqtt5Publish.builder()
            .topic(topic)
            .payload(message.toByteArray(Charsets.UTF_8))
            .qos(qos)
            .retain(retain)
            .build()
        client.client.publish(publish)
    }
}
