package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.hivemq.client.mqtt.datatypes.MqttQos
import gent.zeus.guitar.DataResult
import gent.zeus.guitar.Environment
import gent.zeus.guitar.data.Preset
import gent.zeus.guitar.logExceptionWarn
import gent.zeus.guitar.mqttold.MqttVoteJson
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MqttContext {
    val mqttClient = MqttClient(
        Environment.MQTT_HOST,
        Environment.MQTT_PORT.toInt(),  // TODO: exception handling for port.toInt
    )
    val mqttPublisher = MqttPublisher(mqttClient, MqttQos.AT_MOST_ONCE, true)

    val mutex = Mutex()
    var currentTrackId: String? = null
    var currentStartTime: Long? = null

    suspend fun startMqtt() {
        if (Environment.MQTT_HOST.isEmpty()) return

        with(MqttListener(mqttClient)) {
            addCallback(Environment.MQTT_ZODOM_LISTEN_TOPIC, ::handleVotes)
            addCallback(Environment.MQTT_LIBRESPOT_LISTEN_TOPIC, ::handlePlaying)
            startListening()
        }
    }

    private suspend fun handleVotes(jsonString: String) = logExceptionWarn("error decoding json from mqtt") {
        val id = mutex.withLock { currentTrackId } ?: return@logExceptionWarn
        val startTime = mutex.withLock { currentStartTime } ?: return@logExceptionWarn

        val votesJson = jacksonObjectMapper().readValue<MqttVoteJson>(jsonString)
        publishTrack(id, startTime, votesJson.votesFor, votesJson.votesAgainst)
    }

    private suspend fun handlePlaying(jsonString: String) = logExceptionWarn("error decoding json from mqtt") {
        val playingJson = jacksonObjectMapper().readValue<MqttPlayingJson>(jsonString)
        publishTrack(playingJson.trackId, System.currentTimeMillis() - playingJson.positionMs)
    }

    private fun publishTrack(id: String, startMs: Long, votesFor: Int? = null, votesAgainst: Int? = null) {
        val track = when (val it =
            if (votesFor == null || votesAgainst == null)
                Preset.Track.details.getModel(id, true)
            else
                Preset.Track.voteless.getModel(id, true)
        ) {
            is DataResult.Ok -> it.value
            is DataResult.Error<*> -> return
        }

        val endMs = startMs - (track.durationInMs ?: 0)
        val publish = MqttDetailJson(
            spotifyId = track.spotifyId,
            name = track.name,
            album = track.album?.name,
            durationInMs = track.durationInMs,
            startedAtMs = startMs,
            endsAtMs = endMs,
            imageUrl = track.imageUrl,
            artists = track.artists?.mapNotNull { it.name } ?: emptyList(),
            votesFor = votesFor ?: track.votesFor,
            votesAgainst = votesAgainst ?: track.votesAgainst
        )

        mqttPublisher.publish(
            Environment.MQTT_PUBLISH_TOPIC,
            jacksonObjectMapper().writeValueAsString(publish),
        )
    }
}
