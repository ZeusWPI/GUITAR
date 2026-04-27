package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gent.zeus.guitar.Environment
import gent.zeus.guitar.PlayerState
import gent.zeus.guitar.isModuleEnabledAndLog
import gent.zeus.guitar.logExceptionWarn
import kotlinx.coroutines.sync.withLock

fun MqttListener.listenLibrespot(context: MqttContext) {
    if (!isModuleEnabledAndLog("mqtt librespot listening", Environment::MQTT_LIBRESPOT_LISTEN_TOPIC)) return

    addCallback(Environment.MQTT_LIBRESPOT_LISTEN_TOPIC, context::handlePlaying)
}

private suspend fun MqttContext.handlePlaying(jsonString: String) = logExceptionWarn("error decoding json from mqtt") {
    val playingJson = jacksonObjectMapper().readValue<MqttPlayingJson>(jsonString)
    PlayerState.mutex.withLock {
        PlayerState.currentTrackId = playingJson.trackId
        PlayerState.currentStartTime = System.currentTimeMillis() - playingJson.positionMs
    }
    publishTrack(playingJson.trackId, System.currentTimeMillis() - playingJson.positionMs)
}

/**
 * json data sent on librespot topic
 */
internal data class MqttPlayingJson(
    val trackId: String,
    val positionMs: Int,
)
