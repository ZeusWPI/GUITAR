package gent.zeus.guitar.mqtt

import gent.zeus.guitar.logger


const val SLEEP_TIME: Long = 1000 * 30 * 1

/**
 * checks every few minutes if mqtt connection is dead and revives it if necessary
 */
class MqttDefibrillator(val client: MqttCallbackClient) : Runnable {

    override fun run() {
        logger.info("defibrillator: looking for dead mqtt connections...")

        while (true) {
            Thread.sleep(SLEEP_TIME)

            if (!client.isConnected) {
                logger.info("defibrillator: dead mqtt connection found!! :O reviving...")
                try {
                    client.connect()
                } catch (_: Exception) {
                    logger.info("defibrillator: could not connect, trying again in ${SLEEP_TIME / 1000} seconds")
                }
            }
        }
    }
}
