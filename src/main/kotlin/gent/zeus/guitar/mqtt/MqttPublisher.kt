package gent.zeus.guitar.mqtt

interface MqttPublisher {
    fun publishTrackDetails(id: String)
}
