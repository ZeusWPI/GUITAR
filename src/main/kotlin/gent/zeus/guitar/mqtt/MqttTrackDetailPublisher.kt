package gent.zeus.guitar.mqtt

import gent.zeus.guitar.Logging
import gent.zeus.guitar.data.DataProvider
import org.eclipse.paho.client.mqttv3.MqttClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
internal class MqttTrackDetailPublisher(client: MqttClient) : MqttPublisher(client) {

    @Autowired
    lateinit var dataProvider: DataProvider

    override fun publishTrackDetails(id: String) {
        Logging.log.info("mqtt: publishing details for track $id")

        val obj = dataProvider.getTrack(id)
        obj ?: return
        this.publishObject(obj)
    }
}
