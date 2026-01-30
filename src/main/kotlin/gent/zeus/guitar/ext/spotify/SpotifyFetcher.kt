package gent.zeus.guitar.ext.spotify

import gent.zeus.guitar.*
import gent.zeus.guitar.data.MusicModel
import gent.zeus.guitar.ext.ModelFiller
import java.util.TreeMap

abstract class SpotifyFetcher<T : MusicModel>(
    protected val spotifyObjectType: SpotifyObjectType
) : ModelFiller<T> {
    /**
     * fetch data and put it in the given MusicalObject
     */
    protected inline fun <reified J : SpotifyJson> getSpotifyJson(id: String): DataResult<J> {
        if (!checkBase62(id)) return DataResult.Error(InvalidIdError())
        getCache(id)?.let {
            if (it is J) return DataResult.Ok(it)
        }

        with(
            httpRequestIntoObj<J>(
                "${SPOTIFY_API_URL}/${spotifyObjectType.apiUrlPrefix}/${id}",
                "${SpotifyToken.get()}"
            )
        ) {
            return when (this) {
                is HttpResponse.Ok -> DataResult.Ok(body.also { putCache(id, it) })
                is HttpResponse.Error -> when (statusCode) {
                    404 -> DataResult.Error(TrackNotFoundError(body))
                    else -> DataResult.Error(SpotifyError(body))
                }
            }
        }
    }

    companion object {
        private val cache: MutableMap<String, SpotifyJson> = TreeMap()

        /**
         * put item with `id` in the cache
         */
        fun putCache(id: String, json: SpotifyJson) {
            cache[id] = json
            logger.debug("wrote item to cache: $id")
        }

        /**
         * retrieve item with `id` from the cache
         */
        fun getCache(id: String): SpotifyJson? = cache[id].also {
            if (it != null) logger.debug("retrieved item from cache: $id")
        }
    }
}

enum class SpotifyObjectType(val apiUrlPrefix: String, val typeString: String) {
    TRACK("tracks", "track"),
    ALBUM("albums", "album"),
    ARTIST("artists", "artist"),
}
