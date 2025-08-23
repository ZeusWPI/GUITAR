package gent.zeus.guitar.spotify

import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track
import org.springframework.web.client.body

class TrackFetcher(id: String) : Fetcher<Track>(id, SpotifyObjectType.TRACK) {
    override fun fetch(): Track? = makeApiRequest().body<SpotifyTrackJson>()
        .takeIfNotNullOrLog()?.let { trackJson ->
            val artists: List<Artist?> = trackJson.artists.map { artistJson ->
                if (artistJson == null) null else
                    Artist(
                        artistJson.id,
                        artistJson.name,
                    )
            }
            val album: Album? = trackJson.album.takeIf { it != null }?.let { albumJson ->
                Album(
                    albumJson.id,
                    albumJson.name,
                )
            }
            Track(
                trackJson.id,
                trackJson.name,
                album,
                artists,
            )
        }
}