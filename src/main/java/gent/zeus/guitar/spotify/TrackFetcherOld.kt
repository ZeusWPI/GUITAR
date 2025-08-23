package gent.zeus.guitar.spotify

import gent.zeus.guitar.Logging
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.SPOTIFY_API_URL
import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.body


class TrackFetcherOld(val id: String) {
    fun fetchDetails(): Track? {
        val trackJson = REST_CLIENT.get()
            .uri("${SPOTIFY_API_URL}/tracks/$id")
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${SpotifyToken.get()}")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<SpotifyTrackJson>()

        if (trackJson == null) {
            Logging.log.error("error fetching track with id ${id}: response body was null")
            return null
        }

        val artists: List<Artist?> = trackJson.artists.map { artistJson ->
            if (artistJson == null) null else
                Artist(
                    artistJson.id,
                    artistJson.name,
                )
        }
        val album: Album = trackJson.let {
            Album(
                it.id,
                it.name,
            )
        }
        return trackJson.let {
            Track(
                it.id,
                it.name,
                album,
                artists
            )
        }
    }
}
