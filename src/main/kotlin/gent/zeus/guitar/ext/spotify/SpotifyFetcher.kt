package gent.zeus.guitar.ext.spotify

import gent.zeus.guitar.*
import gent.zeus.guitar.data.MusicModel
import gent.zeus.guitar.ext.DataFiller

abstract class SpotifyFetcher<T : MusicModel>(
    protected val id: String,
    protected val spotifyObjectType: SpotifyObjectType
) : DataFiller<T> {
    /**
     * fetch data and put it in the given MusicalObject
     */
    protected inline fun <reified J : SpotifyJson> getSpotifyJson(): DataResult<J> =
        with(
            makeAuthorizedRequest<J>(
                "${SPOTIFY_API_URL}/${spotifyObjectType.apiUrlPrefix}/${id}",
                "${SpotifyToken.get()}"
            )
        ) {
            return when (statusCode.value()) {
                200 if body != null -> DataResult.Ok(body!!)
                404 -> DataResult.Error(TrackNotFoundError())
                else -> DataResult.Error<_, J>(SpotifyError()).also {
                    logger.debug("error fetching {} with id {}: {}", spotifyObjectType.typeString, id, it)
                }
            }
        }
}

enum class SpotifyObjectType(val apiUrlPrefix: String, val typeString: String) {
    TRACK("tracks", "track"),
    ALBUM("albums", "album"),
    ARTIST("artists", "artist"),
}
