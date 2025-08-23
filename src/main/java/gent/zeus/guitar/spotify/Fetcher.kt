package gent.zeus.guitar.spotify

import gent.zeus.guitar.Logging
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.SPOTIFY_API_URL
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

abstract class Fetcher(val id: String) {
    protected fun makeApiRequest(prefix: SpotifyObjectType): RestClient.ResponseSpec =
        REST_CLIENT.get()
            .uri("${SPOTIFY_API_URL}/${prefix.apiUrlPrefix}/${id}")
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${SpotifyToken.get()}")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()

    fun SpotifyJson?.takeIfNotNullOrLog(spotifyObjectType: SpotifyObjectType) =
        if (this == null) {
            Logging.log.error("error fetching ${spotifyObjectType.typeString} with id ${id}: response body was null")
            null
        } else {
            this
        }
}

enum class SpotifyObjectType(val apiUrlPrefix: String, val typeString: String) {
    TRACKS("tracks", "track"),
    ALBUMS("albums", "album"),
    ARTISTS("artists", "artist"),
}
