package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gent.zeus.guitar.data.DataProvider
import gent.zeus.guitar.logger
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttCallbackClient : MqttCallback {

    private val dataProvider = DataProvider()

    private val mqttClient: MqttClient = MqttClient(
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

    fun connect(vararg topics: String) {
        logger.info("mqtt: connecting...")
        mqttClient.connect(mqttOptions)
        topics.forEach { mqttClient.subscribe(it) }
        logger.info("mqtt: connected to ${MqttEnv.hostString} with id ${MqttEnv.clientId}")
    }

    val isConnected get() = mqttClient.isConnected

    override fun connectionLost(cause: Throwable?) {
        logger.warn("mqtt: connection lost, reconnecting...", cause)
        connect()
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        logger.info("mqtt: received message on $topic")
        message ?: return
        when (topic) {
            MqttEnv.LIBRESPOT_LISTEN_TOPIC -> handleLibrespotMessage(message)
            MqttEnv.ZODOM_LISTEN_TOPIC -> handleZodomMessage(message)
        }
    }

    private var currentSong: MqttPlayingJson? = null

    private fun handleLibrespotMessage(message: MqttMessage) {
        val playingJson = try {
            val playingJson: MqttPlayingJson = jacksonObjectMapper().readValue(String(message.payload))
            playingJson
        } catch (e: JsonParseException) {
            logger.warn("mqtt: error decoding playing json: ${e.message}")
            null
        } catch (e: Exception) {
            logger.warn("mqtt: error handling Librespot message: ${e.message}")
            null
        }
        playingJson ?: return

        currentSong = playingJson
        sendCurrentSongData()
    }

    private fun sendCurrentSongData() {
        currentSong?.let {  // doing it like this to prevent concurrency warnings
            publisher.publishTrackDetails(it.trackId, it.positionMs)
        }
    }

    /**
     * removes a track from the cache when someone votes on it
     * TODO: change this behaviour
     */
    private fun handleZodomMessage(message: MqttMessage) {
        val votesJson = try {
            val votesJson: MqttVoteJson = jacksonObjectMapper().readValue(String(message.payload))
            votesJson
        } catch (e: JsonParseException) {
            logger.warn("mqtt: error decoding votes json: ${e.message}")
            null
        } catch (e: Exception) {
            logger.warn("mqtt: error error handling Zodom message: ${e.message}")
            null
        }
        votesJson?.songId ?: return

        dataProvider.removeTrack(votesJson.songId)
        sendCurrentSongData()
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
    }
}