package gent.zeus.guitar.spotify

import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track
import org.springframework.web.client.body

class AlbumFetcher(id: String) : SpotifyFetcher<Album>(id, SpotifyObjectType.ALBUM) {
    override fun fetch(): Album? = makeApiRequest().body<SpotifyAlbumJson>()
        .takeIfNotNullOrLog()?.let { albumJson ->
            val tracks: List<Track?> = albumJson.tracks?.items?.map { trackJson ->
                if (trackJson == null) return null

                val artists: List<Artist?> = trackJson.artists.map { artistJson ->
                    if (artistJson == null) return null
                    Artist(
                        artistJson.id,
                        artistJson.name
                    )
                }

                Track(
                    trackJson.id,
                    trackJson.name,
                    null,
                    artists,
                )
            } ?: emptyList()

            val artists: List<Artist?> = albumJson.artists?.map { artistJson ->
                if (artistJson == null) return null
                Artist(
                    artistJson.id,
                    artistJson.name
                )
            } ?: emptyList()

            Album(
                albumJson.id,
                albumJson.name,
                tracks,
                artists,
            )
        }
}
