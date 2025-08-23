package gent.zeus.guitar

import org.springframework.web.client.RestClient

object Global {
    const val SPOTIFY_API_URL = "https://api.spotify.com/v1"

    val REST_CLIENT: RestClient = RestClient.create()
}
