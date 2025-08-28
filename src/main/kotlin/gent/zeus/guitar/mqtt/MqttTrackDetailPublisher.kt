package gent.zeus.guitar.mqtt

import gent.zeus.guitar.Logging
import gent.zeus.guitar.data.DataProvider
import org.eclipse.paho.client.mqttv3.MqttClient


internal class MqttTrackDetailPublisher(client: MqttClient, val dataProvider: DataProvider) : MqttPublisher(client) {

    override fun publishTrackDetails(id: String) {
        Logging.log.info("mqtt: publishing details for track $id")

        val detailObj = dataProvider.getTrack(id)
        detailObj ?: return

        val mqttDetailJson: MqttDetailJson = detailObj.let { detailObj ->
            MqttDetailJson(
                name = detailObj.name,
                album = detailObj.album?.name,
                durationInMs = detailObj.durationInMs,
                startedAtMs = System.currentTimeMillis(),
                endsAtMs = detailObj.durationInMs?.plus(System.currentTimeMillis()),
                spotifyId = detailObj.spotifyId,
                imageUrl = detailObj.imageUrl,
                artists = detailObj.artists?.mapNotNull { artist -> artist.name } ?: emptyList(),
            )
        }
        this.publishObject(mqttDetailJson)
    }
}
