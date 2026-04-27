package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hivemq.client.mqtt.datatypes.MqttQos
import gent.zeus.guitar.DataResult
import gent.zeus.guitar.Environment
import gent.zeus.guitar.data.Preset
import gent.zeus.guitar.isModuleEnabledAndLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MqttContext {
    val mqttClient = MqttClient(
        Environment.MQTT_HOST,
        Environment.MQTT_PORT.toInt(),  // TODO: exception handling for port.toInt
    )
    val mqttPublisher = MqttPublisher(mqttClient, MqttQos.AT_MOST_ONCE, true)

    suspend fun startMqtt() {
        with(MqttListener(mqttClient)) {
            listenLibrespot(this@MqttContext)
            listenZodom(this@MqttContext)
            listenMpris(this@MqttContext)
            startListening()
        }
    }

    internal fun publishTrack(id: String, startMs: Long, votesFor: Int? = null, votesAgainst: Int? = null) {
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

/**
 * json data sent by guitar
 */
internal data class MqttDetailJson(
    val spotifyId: String,
    val name: String?,
    val album: String?,
    val durationInMs: Int?,
    val startedAtMs: Long,
    val endsAtMs: Long?,
    val imageUrl: String?,
    val artists: List<String>,
    val votesFor: Int?,
    val votesAgainst: Int?,
)

fun CoroutineScope.startMqtt() = launch {
    val enable = isModuleEnabledAndLog("mqtt", Environment::MQTT_HOST, warnDisabled = true)
    if (enable) MqttContext().startMqtt()
}
