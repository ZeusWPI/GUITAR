package gent.zeus.guitar.mqtt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.hivemq.client.mqtt.datatypes.MqttQos
import gent.zeus.guitar.Environment
import gent.zeus.guitar.logExceptionWarn
import gent.zeus.guitar.logger
import gent.zeus.guitar.mqttold.MqttVoteJson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties
import kotlin.time.Duration.Companion.seconds

suspend fun startMqtt() = coroutineScope {
    if (Environment.MQTT_HOST.isEmpty()) return@coroutineScope

    val mqttClient = MqttClient(
        Environment.MQTT_HOST,
        Environment.MQTT_PORT.toInt(),  // TODO: exception handling for port.toInt
    )
    val mqttPublisher = MqttPublisher(mqttClient, MqttQos.AT_MOST_ONCE, true)
    with(MqttListener(mqttClient)) {
        addCallback("guitartest1") { logger.info("guitartest1!!!!") }
        addCallback("guitartest2") { logger.info("guitartest2!!!!") }
        startListening()
    }
}

private fun handleVotes(jsonString: String) = logExceptionWarn("error decoding json from mqtt") {
    jacksonObjectMapper().readValue<MqttVoteJson>(jsonString)
}

private fun handlePlaying(jsonString: String) = logExceptionWarn("error decoding json from mqtt") {
    jacksonObjectMapper().readValue<MqttPlayingJson>(jsonString)
}
