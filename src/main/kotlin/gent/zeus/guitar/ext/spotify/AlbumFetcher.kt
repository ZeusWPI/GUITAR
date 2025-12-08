package gent.zeus.guitar.ext.spotify

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track

class AlbumFetcher(id: String) : SpotifyFetcher<Album>(id, SpotifyObjectType.ALBUM) {
    override fun fetchInto(musicModel: Album): DataResult<Album> {
        val albumJson = when (val response = getSpotifyJson<SpotifyAlbumJson>()) {
            is DataResult.Ok -> response.value
            is DataResult.Error<*, *> -> return DataResult.Error(response.error)
        }

        return DataResult.Ok(
            musicModel.copy(
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
                        durationInMs = null,
                        imageUrl = null,
                        spotifyUrl = null,
                        votesFor = null,
                        votesAgainst = null,
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
            ))
    }
}
