package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gent.zeus.guitar.Environment
import gent.zeus.guitar.isModuleEnabledAndLog
import gent.zeus.guitar.logStacktrace

fun MqttListener.listenMpris(context: MqttContext) {
    if (!isModuleEnabledAndLog("mqtt mpris listening", Environment::MQTT_MPRIS_LISTEN_TOPIC)) return

    addCallback(Environment.MQTT_MPRIS_LISTEN_TOPIC, context::handleMpris)
}

private val spotifyPrefixRegex = """^/com/spotify/track""".toRegex()

private suspend fun MqttContext.handleMpris(jsonString: String) {
    try {
        val playing = jacksonObjectMapper().readValue<MprisPlayingJson>(jsonString)

        // we can't handle anything else than spotify yet
        if (!playing.metadata.trackId.contains(spotifyPrefixRegex)) return
        val id = spotifyPrefixRegex.replace(playing.metadata.trackId, "")

        publishTrack(id, System.currentTimeMillis() - playing.positionMs)
    } catch (e: Exception) {
        e.logStacktrace("error decoding json")
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
private data class MprisPlayingJson(
    @JsonProperty("Metadata") val metadata: MprisMetadata,
    @JsonProperty("Position") val position: Long,
) {
    val positionMs get() = position / 1000
}

@JsonIgnoreProperties(ignoreUnknown = true)
private data class MprisMetadata(
    @JsonProperty("xesam:url") val url: String,
    @JsonProperty("mpris:trackid") val trackId: String,
)
