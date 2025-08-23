package gent.zeus.guitar.spotify

import gent.zeus.guitar.Logging
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.SPOTIFY_API_URL
import gent.zeus.guitar.data.MusicalObject
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

abstract class Fetcher<T : MusicalObject>(private val id: String, private val spotifyObjectType: SpotifyObjectType) {
    abstract fun fetch(): T?

    protected fun makeApiRequest(): RestClient.ResponseSpec =
        REST_CLIENT.get()
            .uri("${SPOTIFY_API_URL}/${spotifyObjectType.apiUrlPrefix}/${id}")
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${SpotifyToken.get()}")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()

    protected fun <J : SpotifyJson> J?.takeIfNotNullOrLog() =
        if (this == null) {
            Logging.log.error("error fetching ${spotifyObjectType.typeString} with id ${id}: response body was null")
            null
        } else {
            this
        }
}

enum class SpotifyObjectType(val apiUrlPrefix: String, val typeString: String) {
    TRACK("tracks", "track"),
    ALBUM("albums", "album"),
    ARTIST("artists", "artist"),
}
