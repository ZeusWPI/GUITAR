package gent.zeus.guitar.spotify

import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track
import org.springframework.web.client.body

class AlbumFetcher(id: String) : SpotifyFetcher<Album>(id, SpotifyObjectType.ALBUM) {
    override fun fetch(): Album? = makeApiRequest().body<SpotifyAlbumJson>()
        .takeIfNotNullOrLog()?.let { albumJson ->
            Album(
                spotifyId = albumJson.id,
                name = albumJson.name,
                tracks = albumJson.tracks?.items?.map { trackJson ->
                    Track(
                        spotifyId = trackJson.id,
                        name = trackJson.name,
                        album = null,
                        artists = trackJson.artists?.map { artistJson ->
                            Artist(
                                spotifyId = artistJson.id,
                                name = artistJson.name,
                                genres = null,
                                spotifyUrl = null,
                            )
                        } ?: emptyList(),
                        spotifyUrl = null,
                    )
                } ?: emptyList(),
                artists = albumJson.artists?.map { artistJson ->
                    Artist(
                        spotifyId = artistJson.id,
                        name = artistJson.name,
                        genres = null,
                        spotifyUrl = null,
                    )
                } ?: emptyList(),
                spotifyUrl = albumJson.externalUrls?.spotify,
            )
        }
}
