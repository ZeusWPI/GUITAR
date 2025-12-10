package gent.zeus.guitar.ext.spotify

import gent.zeus.guitar.*
import gent.zeus.guitar.data.MusicModel
import gent.zeus.guitar.ext.ModelFiller
import org.slf4j.Logger

abstract class SpotifyFetcher<T : MusicModel>(
    protected val spotifyObjectType: SpotifyObjectType
) : ModelFiller<T> {
    /**
     * fetch data and put it in the given MusicalObject
     */
    protected inline fun <reified J : SpotifyJson> getSpotifyJson(id: String): DataResult<J> =
        with(
            httpRequestIntoObj<J>(
                "${SPOTIFY_API_URL}/${spotifyObjectType.apiUrlPrefix}/${id}",
                "${SpotifyToken.get()}"
            )
        ) {
            logger.error(this.toString())
            return when (this) {
                is HttpResponse.Ok if body != null -> DataResult.Ok(body)
                is HttpResponse.Error -> when (statusCode) {
                    404 -> DataResult.Error(TrackNotFoundError())
                    else -> DataResult.Error(SpotifyError())
                }

                else -> DataResult.Error(SpotifyError())  // success response but body was null
            }
        }
}

enum class SpotifyObjectType(val apiUrlPrefix: String, val typeString: String) {
    TRACK("tracks", "track"),
    ALBUM("albums", "album"),
    ARTIST("artists", "artist"),
}
