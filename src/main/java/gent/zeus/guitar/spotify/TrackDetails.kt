package gent.zeus.guitar.spotify

import gent.zeus.guitar.Global
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.body

// xhs
// -A bearer
// -a BQC1_oL4gWmWgWWRBf_-0rtZCNVC_UB1rC3muEKFcV4kUbZixQT1TAy2f39dYVmnOQkwouKX2IzcglgOru85Zbo1IAmtC1OW78Qc1sHlBjZEhoTTnSu2rdVhab-vnfqUFzLqyuDYcp4
// api.spotify.com/v1/tracks/5g1POu3HMfYQ0OX4lzlU6D

data class TrackDetails(
    val title: String,
    val artist: String,
    val album: String,
)

class TrackFetcher(val id: String) {
    fun fetchDetails(): TrackDetails {
        val trackJson = Global.REST_CLIENT.get()
            .uri("${Global.SPOTIFY_API_URL}/tracks/$id")
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${SpotifyGlobal.token}")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<TrackJson>()

        return TrackDetails(
            trackJson?.name ?: "unknown",
            "none", // trackJson?.artist?.name ?: "unknown",
            trackJson?.album?.name ?: "unknown",
        )
    }

    private data class TrackJson(
        val name: String,
        val durationMs: Int,
        // val artist: ArtistJson,
        val album: AlbumJson,
    )

    private data class ArtistJson(
        val name: String,
    )

    private data class AlbumJson(
        val name: String,
    )
}
