package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gent.zeus.guitar.Environment
import gent.zeus.guitar.PlayerState
import gent.zeus.guitar.isModuleEnabledAndLog
import gent.zeus.guitar.logExceptionWarn
import kotlinx.coroutines.sync.withLock

fun MqttListener.listenLibrespot(context: MqttContext) {
    val enable = isModuleEnabledAndLog("mqtt librespot listening", Environment::MQTT_LIBRESPOT_LISTEN_TOPIC)
    if (!enable) return

    addCallback(Environment.MQTT_ZODOM_LISTEN_TOPIC, context::handleVotes)
    addCallback(Environment.MQTT_LIBRESPOT_LISTEN_TOPIC, context::handlePlaying)
}

private suspend fun MqttContext.handleVotes(jsonString: String) = logExceptionWarn("error decoding json from mqtt") {
    val id = PlayerState.mutex.withLock { PlayerState.currentTrackId } ?: return@logExceptionWarn
    val startTime = PlayerState.mutex.withLock { PlayerState.currentStartTime } ?: return@logExceptionWarn

    val votesJson = jacksonObjectMapper().readValue<MqttVoteJson>(jsonString)
    if (votesJson.songId != id) return@logExceptionWarn
    publishTrack(id, startTime, votesJson.votesFor, votesJson.votesAgainst)
}

private suspend fun MqttContext.handlePlaying(jsonString: String) = logExceptionWarn("error decoding json from mqtt") {
    val playingJson = jacksonObjectMapper().readValue<MqttPlayingJson>(jsonString)
    PlayerState.mutex.withLock {
        PlayerState.currentTrackId = playingJson.trackId
        PlayerState.currentStartTime = System.currentTimeMillis() - playingJson.positionMs
    }
    publishTrack(playingJson.trackId, System.currentTimeMillis() - playingJson.positionMs)
}
