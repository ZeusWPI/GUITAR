package gent.zeus.guitar.ext.spotify

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track

class TrackFetcher() : SpotifyFetcher<Track>(SpotifyObjectType.TRACK) {
    override fun fetchInto(musicModel: Track): DataResult<Track> {
        val trackJson = when (val response = getSpotifyJson<SpotifyTrackJson>(musicModel.spotifyId)) {
            is DataResult.Ok -> response.value
            is DataResult.Error<*, *> -> return DataResult.Error(response.error)
        }

        return DataResult.Ok(
            musicModel.copy(
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
                imageUrl = trackJson.album?.images?.maxBy { imageJson -> imageJson.height }?.url,
                spotifyUrl = trackJson.externalUrls?.spotify,
            )
        )
    }
}