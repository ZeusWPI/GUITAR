package gent.zeus.guitar.spotify

import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track
import org.springframework.web.client.body

class AlbumFetcher(id: String) : SpotifyFetcher<Album>(id, SpotifyObjectType.ALBUM) {
    override fun fetch(): Album? = makeApiRequest().body<SpotifyAlbumJson>()
        .takeIfNotNullOrLog()?.let { albumJson ->
            Album(
                albumJson.id,
                albumJson.name,
                albumJson.tracks?.items?.map { trackJson ->
                    if (trackJson == null) return null
                    Track(
                        trackJson.id,
                        trackJson.name,
                        null,
                        trackJson.artists.map { artistJson ->
                            if (artistJson == null) return null
                            Artist(
                                artistJson.id,
                                artistJson.name
                            )
                        },
                    )
                } ?: emptyList(),
                albumJson.artists?.map { artistJson ->
                    if (artistJson == null) return null
                    Artist(
                        artistJson.id,
                        artistJson.name
                    )
                } ?: emptyList(),
            )
        }
}
