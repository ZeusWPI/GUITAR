package gent.zeus.guitar.spotify

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.SPOTIFY_API_URL
import gent.zeus.guitar.data.Track
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.body

// xhs
// -A bearer
// -a BQC1_oL4gWmWgWWRBf_-0rtZCNVC_UB1rC3muEKFcV4kUbZixQT1TAy2f39dYVmnOQkwouKX2IzcglgOru85Zbo1IAmtC1OW78Qc1sHlBjZEhoTTnSu2rdVhab-vnfqUFzLqyuDYcp4
// api.spotify.com/v1/tracks/5g1POu3HMfYQ0OX4lzlU6D

data class TrackDetails(
    val title: String?,
    val artist: List<String>?,
    val album: String?,
)

class TrackFetcher(val id: String) {
    fun fetchDetails(): Track {
        val trackJson = REST_CLIENT.get()
            .uri("${SPOTIFY_API_URL}/tracks/$id")
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${SpotifyToken.get()}")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<TrackJson>()

        val artists: List<ArtistJson>
        TODO()
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
