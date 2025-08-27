package gent.zeus.guitar.mqtt


internal data class MqttPlayingJson(
    val trackId: String,
    val positionMs: Int,
)
