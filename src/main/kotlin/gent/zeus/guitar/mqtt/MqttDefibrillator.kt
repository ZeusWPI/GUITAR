package gent.zeus.guitar.mqtt

import gent.zeus.guitar.Logging
import org.apache.juli.logging.Log
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.scheduling.config.Task
import java.net.ConnectException


const val SLEEP_TIME: Long = 1000 * 30 * 1

/**
 * checks every few minutes if mqtt connection is dead and revives it if necessary
 */
class MqttDefibrillator(val client: MqttCallbackClient) : Runnable {

    override fun run() {
        Logging.log.info("defibrillator: looking for dead mqtt connections...")

        while (true) {
            Thread.sleep(SLEEP_TIME)

            if (!client.isConnected) {
                Logging.log.info("defibrillator: dead mqtt connection found!! :O reviving...")
                try {
                    client.connect()
                } catch (_: Exception) {
                    Logging.log.info("defibrillator: could not connect, trying again in ${SLEEP_TIME / 1000} seconds")
                }
            }
        }
    }
}
