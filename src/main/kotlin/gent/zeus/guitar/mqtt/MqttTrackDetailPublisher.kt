package gent.zeus.guitar.mqtt

import gent.zeus.guitar.data.DataProvider
import gent.zeus.guitar.logger
import org.eclipse.paho.client.mqttv3.MqttClient


internal class MqttTrackDetailPublisher(client: MqttClient, val dataProvider: DataProvider) : MqttPublisher(client) {

    fun publishTrackDetails(id: String, startMs: Int) {
        logger.info("mqtt: publishing details for track $id")

        val detailObj = dataProvider.getTrack(id)
        detailObj ?: return

        val mqttDetailJson: MqttDetailJson = detailObj.let { detailObj ->
            MqttDetailJson(
                name = detailObj.name,
                album = detailObj.album?.name,
                durationInMs = detailObj.durationInMs,
                startedAtMs = System.currentTimeMillis() - startMs,
                endsAtMs = detailObj.durationInMs?.plus(System.currentTimeMillis())?.minus(startMs),
                spotifyId = detailObj.spotifyId,
                imageUrl = detailObj.imageUrl,
                artists = detailObj.artists?.mapNotNull { artist -> artist.name } ?: emptyList(),
                votesFor = detailObj.votesFor,
                votesAgainst = detailObj.votesAgainst,
            )
        }
        this.publishObject(mqttDetailJson)
    }
}
