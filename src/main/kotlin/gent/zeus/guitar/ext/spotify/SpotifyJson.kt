package gent.zeus.guitar.ext.spotify

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * data classes mapping to spotify api response json
 */

sealed interface SpotifyJson

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal data class SpotifyTrackJson(
    val album: SpotifyAlbumJson?,
    val artists: List<SpotifyArtistJson>?,
    val durationMs: Int?,
    val id: String,
    val name: String?,
    val externalUrls: SpotifyUrl?,
) : SpotifyJson


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal data class SpotifyAlbumJson(
    val totalTracks: Int?,
    val id: String,
    val images: List<SpotifyImageJson>?,
    val name: String?,
    val artists: List<SpotifyArtistJson>?,
    val tracks: SpotifyAlbumTracklistJson?,
    val externalUrls: SpotifyUrl?
) : SpotifyJson

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal data class SpotifyAlbumTracklistJson(
    val items: List<SpotifyTrackJson>,
)


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal data class SpotifyArtistJson(
    val genres: List<String>?,
    val name: String?,
    val id: String,
    val images: List<SpotifyImageJson>?,
    val externalUrls: SpotifyUrl?
) : SpotifyJson


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal data class SpotifyImageJson(
    val url: String,
    val height: Int,
    val width: Int,
)


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal data class SpotifyUrl(
    val spotify: String,
)
