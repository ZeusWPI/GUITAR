package gent.zeus.guitar.spotify

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import gent.zeus.guitar.Logging
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.SPOTIFY_API_URL
import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.body


data class TrackDetails(
    val title: String?,
    val artist: List<String>?,
    val album: String?,
)

class TrackFetcher(val id: String) {
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

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    private data class TrackJson(
        @JsonProperty("name") val name: String,
        val durationMs: Int,
        val artists: List<ArtistJson>,
        val album: AlbumJson,
    )

    private data class ArtistJson(
        val name: String,
    )

    private data class AlbumJson(
        val name: String,
    )
}
