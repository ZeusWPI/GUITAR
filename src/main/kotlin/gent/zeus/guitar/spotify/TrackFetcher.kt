package gent.zeus.guitar.spotify

import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track

class TrackFetcher(id: String) : SpotifyFetcher<Track>(id, SpotifyObjectType.TRACK) {
    override fun fetch(): Track? = getSpotifyJson<SpotifyTrackJson>()?.let { trackJson ->
        Track(
            spotifyId = trackJson.id,
            name = trackJson.name,
            album = trackJson.album?.let { albumJson ->
                Album(
                    spotifyId = albumJson.id,
                    name = albumJson.name,
                    tracks = null,
                    artists = albumJson.artists?.map { artistJson ->
                        Artist(
                            spotifyId = artistJson.id,
                            name = artistJson.name,
                            genres = null,
                            spotifyUrl = null,
                        )
                    } ?: emptyList(),
                    spotifyUrl = null,
                )
            },
            artists = trackJson.artists?.map { artistJson ->
                Artist(
                    spotifyId = artistJson.id,
                    name = artistJson.name,
                    genres = null,
                    spotifyUrl = null,
                )
            } ?: emptyList(),
            durationInMs = trackJson.durationMs,
            spotifyUrl = trackJson.externalUrls?.spotify,
        )
    }
}