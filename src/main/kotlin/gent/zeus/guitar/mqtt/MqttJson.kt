package gent.zeus.guitar.mqtt


/**
 * json data sent on librespot topic
 */
internal data class MqttPlayingJson(
    val trackId: String,
    val positionMs: Int,
)

/**
 * json data sent on votes topic
 */
internal data class MqttVoteJson(
    val songId: String?,
    val votesFor: Int?,
    val votesAgainst: Int?,
)

/**
 * json data sent by guitar
 */
internal data class MqttDetailJson(
    val name: String?,
    val album: String?,
    val durationInMs: Int?,
    val startedAtMs: Long,
    val endsAtMs: Long?,
    val spotifyId: String,
    val imageUrl: String?,
    val artists: List<String>,
    val votesFor: Int?,
    val votesAgainst: Int?,
)
