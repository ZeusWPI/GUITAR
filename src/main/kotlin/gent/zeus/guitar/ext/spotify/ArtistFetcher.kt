package gent.zeus.guitar.ext.spotify

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.data.Artist

class ArtistFetcher(id: String) : SpotifyFetcher<Artist>(id, SpotifyObjectType.ARTIST) {
    override fun fetchInto(musicModel: Artist): DataResult<Artist> {
        val artistJson = when (val response = getSpotifyJson<SpotifyArtistJson>()) {
            is DataResult.Ok -> response.value
            is DataResult.Error<*, *> -> return DataResult.Error(response.error)
        }

        return DataResult.Ok(
            musicModel.copy(
                spotifyId = artistJson.id,
                name = artistJson.name,
                genres = artistJson.genres ?: emptyList(),
                spotifyUrl = artistJson.externalUrls?.spotify,
            )
        )
    }
}
