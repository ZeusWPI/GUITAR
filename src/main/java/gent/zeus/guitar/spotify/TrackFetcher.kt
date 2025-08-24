package gent.zeus.guitar.spotify

import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track
import org.springframework.web.client.body

class TrackFetcher(id: String) : SpotifyFetcher<Track>(id, SpotifyObjectType.TRACK) {
    override fun fetch(): Track? = makeApiRequest().body<SpotifyTrackJson>()
        .takeIfNotNullOrLog()?.let { trackJson ->
            Track(
                trackJson.id,
                trackJson.name,
                trackJson.album?.let { albumJson ->
                    Album(
                        albumJson.id,
                        albumJson.name,
                        null,
                        albumJson.artists?.map { artistJson ->
                            Artist(
                                artistJson.id,
                                artistJson.name,
                                null,
                                null,
                            )
                        } ?: emptyList(),
                        null,
                    )
                },
                trackJson.artists?.map { artistJson ->
                    Artist(
                        artistJson.id,
                        artistJson.name,
                        null,
                        null,
                    )
                } ?: emptyList(),
                trackJson.externalUrls?.spotify,
            )
        }
}