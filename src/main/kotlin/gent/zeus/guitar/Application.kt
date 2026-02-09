package gent.zeus.guitar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    Environment.SPOTIFY_CLIENT_ID  // access variable to make singleton object

    runApplication<Application>(*args)
}
