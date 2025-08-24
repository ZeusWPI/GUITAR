package gent.zeus.guitar.spotify

import gent.zeus.guitar.data.Artist
import org.springframework.web.client.body

class ArtistFetcher(var id: String) : SpotifyFetcher<Artist>(id, SpotifyObjectType.ARTIST) {
    override fun fetch(): Artist? = makeApiRequest().body<SpotifyArtistJson>()
        .takeIfNotNullOrLog()?.let { artistJson ->
            Artist(
                spotifyId = artistJson.id,
                name = artistJson.name,
                genres = artistJson.genres ?: emptyList(),
                spotifyUrl = artistJson.externalUrls?.spotify,
            )
        }
}