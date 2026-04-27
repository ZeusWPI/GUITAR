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

        val startTime = System.currentTimeMillis() - playing.positionMs
        updateCurrent(id, startTime, playing.paused)
        publishTrack(id, startTime, playing.paused)
    } catch (e: Exception) {
        e.logStacktrace("error decoding json")
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
private data class MprisPlayingJson(
    @JsonProperty("Metadata") val metadata: MprisMetadata,
    @JsonProperty("Position") val position: Long,
    @JsonProperty("PlaybackStatus") private val playbackStatusStr: String,
    @JsonProperty("LoopStatus") private val loopStatusStr: String,
) {
    val positionMs get() = position / 1000
    val paused get() = playbackStatusStr == "Paused"
    val loopStatus get() = LoopStatus.fromString(loopStatusStr)
}

@JsonIgnoreProperties(ignoreUnknown = true)
private data class MprisMetadata(
    @JsonProperty("xesam:url") val url: String,
    @JsonProperty("mpris:trackid") val trackId: String,
)

private enum class LoopStatus(private val stringName: String) {
    NONE("none"), PLAYLIST("playlist"), TRACK("track");

    override fun toString(): String = stringName

    companion object {
        fun fromString(string: String): LoopStatus = when (string.lowercase()) {
            "none" -> NONE
            "playlist" -> PLAYLIST
            "track" -> TRACK
            else -> throw IllegalArgumentException("$string is not a valid loop status")
        }
    }
}
