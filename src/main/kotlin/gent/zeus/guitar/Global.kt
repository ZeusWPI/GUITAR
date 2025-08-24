package gent.zeus.guitar

import org.springframework.web.client.RestClient

val REST_CLIENT: RestClient = RestClient.create()

const val SPOTIFY_API_URL = "https://api.spotify.com/v1"
