package gent.zeus.guitar.mqtt

import gent.zeus.guitar.Logging
import gent.zeus.guitar.data.DataProvider
import gent.zeus.guitar.data.MusicalObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
internal class MqttTrackDetailPublisher : MqttPublisher {

    @Autowired
    lateinit var dataProvider: DataProvider

    override fun publishTrackDetails(id: String) {
        Logging.log.info("mqtt: publishing details for track $id")
    }

    private fun publishData(musicalObject: MusicalObject) {

    }
}
