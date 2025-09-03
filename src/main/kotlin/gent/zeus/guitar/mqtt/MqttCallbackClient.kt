package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gent.zeus.guitar.Logging
import gent.zeus.guitar.data.DataProvider
import gent.zeus.guitar.storage.InMemoryTrackStore
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttCallbackClient : MqttCallback {

    private val dataProvider = DataProvider()

    private var mqttClient: MqttClient = MqttClient(
        "tcp://${MqttEnv.URL}:${MqttEnv.PORT}",
        MqttEnv.clientId,
        MemoryPersistence(),
    ).apply {
        setCallback(this@MqttCallbackClient)
    }

    private val publisher = MqttTrackDetailPublisher(mqttClient, dataProvider)

    private val mqttOptions = MqttConnectOptions().apply {
        isAutomaticReconnect = false
        isCleanSession = true
        connectionTimeout = 10
    }

    init {
        Thread(
            MqttDefibrillator(this),
            "mqtt-defibrillator"
        ).start()
    }

    fun connect() {
        Logging.log.info("mqtt: connecting...")
        mqttClient.connect(mqttOptions)
        mqttClient.subscribe(MqttEnv.LISTEN_TOPIC)
        Logging.log.info("mqtt: connected to ${MqttEnv.hostString} with id ${MqttEnv.clientId}")
    }

    val isConnected get() = mqttClient.isConnected

    override fun connectionLost(cause: Throwable?) {
        Logging.log.warn("mqtt: connection lost, reconnecting...", cause)
        connect()
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        Logging.log.info("mqtt: received message on $topic")
        message ?: return
        val playingJson = try {
            val playingJson: MqttPlayingJson = with(jacksonObjectMapper()) {
                readValue(
                    String(message.payload)
                )
            }
            playingJson

        } catch (e: JsonParseException) {
            Logging.log.warn("mqtt: error decoding json: ${e.message}")
            null
        } catch (e: Exception) {
            Logging.log.warn("mqtt: error: ${e.message}")
            null
        }
        playingJson ?: return

        publisher.publishTrackDetails(playingJson.trackId, playingJson.positionMs)
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
    }
}