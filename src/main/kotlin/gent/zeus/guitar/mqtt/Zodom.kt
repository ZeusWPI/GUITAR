package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gent.zeus.guitar.Environment
import gent.zeus.guitar.PlayerState
import gent.zeus.guitar.isModuleEnabledAndLog
import gent.zeus.guitar.logExceptionWarn
import kotlinx.coroutines.sync.withLock

fun MqttListener.listenZodom(context: MqttContext) {
    if (!isModuleEnabledAndLog("mqtt zodom listening", Environment::MQTT_ZODOM_LISTEN_TOPIC)) return

    addCallback(Environment.MQTT_ZODOM_LISTEN_TOPIC, context::handleVotes)
}

private suspend fun MqttContext.handleVotes(jsonString: String) = logExceptionWarn("error decoding json from mqtt") {
    val id = PlayerState.mutex.withLock { PlayerState.currentTrackId } ?: return@logExceptionWarn
    val startTime = PlayerState.mutex.withLock { PlayerState.currentStartTime } ?: return@logExceptionWarn
    val paused = PlayerState.mutex.withLock { PlayerState.paused } ?: return@logExceptionWarn

    val votesJson = jacksonObjectMapper().readValue<MqttVoteJson>(jsonString)
    if (votesJson.songId != id) return@logExceptionWarn
    publishTrack(id, startTime, paused, votesJson.votesFor, votesJson.votesAgainst)
}

/**
 * json data sent on votes topic
 */
internal data class MqttVoteJson(
    val songId: String?,
    val votesFor: Int?,
    val votesAgainst: Int?,
)

