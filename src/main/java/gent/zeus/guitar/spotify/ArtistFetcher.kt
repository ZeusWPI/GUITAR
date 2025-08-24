package gent.zeus.guitar.spotify

import gent.zeus.guitar.data.Artist

class ArtistFetcher(id: String) : SpotifyFetcher<Artist>(id, SpotifyObjectType.ARTIST) {
    override fun fetch(): Artist? = getSpotifyJson<SpotifyArtistJson>()?.let { artistJson ->
        Artist(
            spotifyId = artistJson.id,
            name = artistJson.name,
            genres = artistJson.genres ?: emptyList(),
            spotifyUrl = artistJson.externalUrls?.spotify,
        )
    }
}