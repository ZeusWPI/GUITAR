package gent.zeus.guitar.spotify

import gent.zeus.guitar.DataFetchError
import gent.zeus.guitar.DataResult
import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track

class AlbumFetcher(id: String) : SpotifyFetcher<Album>(id, SpotifyObjectType.ALBUM) {
    override fun fetchInto(musicalObject: Album): DataFetchError? {
        val albumJson = when (val response = getSpotifyJson<SpotifyAlbumJson>()) {
            is DataResult.DataError<*, *> -> return response.error
            is DataResult.DataSuccess<SpotifyAlbumJson> -> response.value
        }

        musicalObject.apply {
            spotifyId = albumJson.id;
            name = albumJson.name;
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
            } ?: emptyList();
            artists = albumJson.artists?.map { artistJson ->
                Artist(
                    spotifyId = artistJson.id,
                    name = artistJson.name,
                    genres = null,
                    spotifyUrl = null,
                )
            } ?: emptyList();
            spotifyUrl = albumJson.externalUrls?.spotify;
        }
        return null
    }
}
