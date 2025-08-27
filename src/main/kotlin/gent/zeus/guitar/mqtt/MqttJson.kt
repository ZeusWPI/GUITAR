package gent.zeus.guitar.mqtt


internal data class MqttPlayingJson(
    val trackId: String,
    val positionMs: Int,
)


internal data class MqttDetailJson(
    val name: String?,
    val album: String?,
    val durationInMs: Int?,
    val endsAt: Long?,
    val spotifyId: String,
    val imageUrl: String?,
    val artists: List<String>,
)
