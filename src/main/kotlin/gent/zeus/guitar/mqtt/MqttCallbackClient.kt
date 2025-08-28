package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gent.zeus.guitar.Logging
import gent.zeus.guitar.data.DataProvider
import gent.zeus.guitar.db.InMemoryTrackStore
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.*

class MqttCallbackClient : MqttCallback {

    private val trackStore = InMemoryTrackStore()
    private val dataProvider = DataProvider(trackStore)

    private lateinit var mqttClientId: String
    private lateinit var mqttClient: MqttClient

    init {
        makeClient()
    }

    private val publisher = MqttTrackDetailPublisher(mqttClient, dataProvider)

    private val mqttOptions = MqttConnectOptions().apply {
        isAutomaticReconnect = false
        isCleanSession = true
        connectionTimeout = 10
    }

    private fun makeClient() {
        mqttClientId = "GUITAR-" + UUID.randomUUID().toString()
        mqttClient = MqttClient(
            "tcp://${MqttEnv.URL}:${MqttEnv.PORT}",
            mqttClientId,
            MemoryPersistence(),
        ).apply {
            setCallback(this@MqttCallbackClient)
        }
        Logging.log.info("mqtt: made new client with id $mqttClientId")
    }

    fun connect() {
        Logging.log.info("mqtt: connecting...")
        mqttClient.connect(mqttOptions)
        mqttClient.subscribe(MqttEnv.LISTEN_TOPIC)
        Logging.log.info("mqtt: connected to ${MqttEnv.hostString} with id $mqttClientId")
    }

    override fun connectionLost(cause: Throwable?) {
        Logging.log.warn("mqtt: connection lost, reconnecting...")
        makeClient()
        connect()
//        reconnect()
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
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

        publisher.publishTrackDetails(playingJson.trackId)
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
    }
}