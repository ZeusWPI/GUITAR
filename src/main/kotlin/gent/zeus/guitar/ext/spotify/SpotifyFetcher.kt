package gent.zeus.guitar.ext.spotify

import gent.zeus.guitar.*
import gent.zeus.guitar.data.MusicModel
import gent.zeus.guitar.ext.ModelFiller
import gent.zeus.guitar.storage.MemoryCache
import java.util.*

abstract class SpotifyFetcher<T : MusicModel>(
    protected val spotifyObjectType: SpotifyObjectType
) : ModelFiller<T> {
    /**
     * fetch data and put it in the given MusicalObject
     */
    protected inline fun <reified J : SpotifyJson> getSpotifyJson(id: String): DataResult<J> {
        cache.get(id)?.let {
            if (it is J) return DataResult.Ok(it)
        }

        with(
            httpRequestIntoObj<J>(
                "${SPOTIFY_API_URL}/${spotifyObjectType.apiUrlPrefix}/${id}",
                "${SpotifyToken.get()}"
            )
        ) {
            return when (this) {
                is HttpResponse.Ok -> DataResult.Ok(body.also { cache.put(id, it) })
                is HttpResponse.Error -> when {
                    statusCode == 404 -> DataResult.Error(TrackNotFoundError(body))
                    body.lowercase().contains("invalid base62 id") -> DataResult.Error(InvalidIdError())
                    else -> DataResult.Error(SpotifyError(body))
                }
            }
        }
    }

    companion object {
        val cache: MemoryCache<String, SpotifyJson> = MemoryCache()
    }
}

enum class SpotifyObjectType(val apiUrlPrefix: String) {
    TRACK("tracks"),
    ALBUM("albums"),
    ARTIST("artists"),
}

fun init() {
    Environment.checkEnvVars(exitOnFail = true) {
        listOf(::SPOTIFY_CLIENT_ID, ::SPOTIFY_CLIENT_SECRET).exists()
    }
}
