package gent.zeus.guitar.ext.spotify

import gent.zeus.guitar.DataFetchError
import gent.zeus.guitar.DataResult
import gent.zeus.guitar.data.Artist

class ArtistFetcher(id: String) : SpotifyFetcher<Artist>(id, SpotifyObjectType.ARTIST) {
    override fun fetchInto(musicalObject: Artist): DataResult<Artist> {
        val artistJson = when (val response = getSpotifyJson<SpotifyArtistJson>()) {
            is DataResult.DataSuccess<SpotifyArtistJson> -> response.value
            is DataResult.DataError<*, *> -> return DataResult.DataError(response.error)
        }

        return DataResult.DataSuccess(
            musicalObject.copy(
                spotifyId = artistJson.id,
                name = artistJson.name,
                genres = artistJson.genres ?: emptyList(),
                spotifyUrl = artistJson.externalUrls?.spotify,
            )
        )
    }
}
