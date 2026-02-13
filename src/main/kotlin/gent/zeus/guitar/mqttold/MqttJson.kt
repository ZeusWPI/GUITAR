package gent.zeus.guitar.mqttold


internal data class MqttPlayingJson(
    val trackId: String,
    val positionMs: Int,
)


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

internal data class MqttVoteJson(
    val songId: String?,
    val votesFor: Int?,
    val votesAgainst: Int?,
)
