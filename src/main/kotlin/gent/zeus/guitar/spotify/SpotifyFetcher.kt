package gent.zeus.guitar.spotify

import gent.zeus.guitar.DataFetchError
import gent.zeus.guitar.DataResult
import gent.zeus.guitar.Logging
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.SPOTIFY_API_URL
import gent.zeus.guitar.data.MusicalObject
import gent.zeus.guitar.makeAuthorizedRequest
import org.apache.juli.logging.Log
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.body

abstract class SpotifyFetcher<T : MusicalObject>(
    protected val id: String,
    protected val spotifyObjectType: SpotifyObjectType
) {
    /**
     * fetch data and put it in the given MusicalObject
     */
    abstract fun fetchInto(musicalObject: T): DataFetchError?

    // TODO handle http error 4xx
    protected inline fun <reified J : SpotifyJson> getSpotifyJson(): DataResult<J> =
        with(
            makeAuthorizedRequest<J>(
                "${SPOTIFY_API_URL}/${spotifyObjectType.apiUrlPrefix}/${id}",
                "${SpotifyToken.get()}"
            )
        ) {
            return when (statusCode.value()) {
                200 if body != null -> DataResult.DataSuccess(body!!)
                404 -> DataResult.DataError(TrackNotFoundError())
                else -> DataResult.DataError<_, J>(SpotifyError()).also {
                    Logging.log.error("error fetching ${spotifyObjectType.typeString} with id $id: $it")
                }
            }
        }
    /*
    REST_CLIENT.get()
        .uri("${SPOTIFY_API_URL}/${spotifyObjectType.apiUrlPrefix}/${id}")
        .header(HttpHeaders.AUTHORIZATION, "Bearer ${SpotifyToken.get()}")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body<J>()
        ?: run {
            Logging.log.error("error fetching ${spotifyObjectType.typeString} with id ${id}: response body was null")
            null
        }

     */
}

enum class SpotifyObjectType(val apiUrlPrefix: String, val typeString: String) {
    TRACK("tracks", "track"),
    ALBUM("albums", "album"),
    ARTIST("artists", "artist"),
}
