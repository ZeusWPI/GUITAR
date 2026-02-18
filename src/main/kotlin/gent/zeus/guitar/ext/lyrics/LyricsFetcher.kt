package gent.zeus.guitar.ext.lyrics

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.HttpResponse
import gent.zeus.guitar.LRCLIB_API_URL
import gent.zeus.guitar.ServerError
import gent.zeus.guitar.UserError
import gent.zeus.guitar.data.Lyrics
import gent.zeus.guitar.data.Track
import gent.zeus.guitar.ext.ModelFiller
import gent.zeus.guitar.httpRequestIntoObj
import gent.zeus.guitar.storage.MemoryCache

class LyricsFetcher : ModelFiller<Track> {
    override fun fetchInto(musicModel: Track): DataResult<Track> {
        cache.get(musicModel.spotifyId)?.let { lyrics ->
            musicModel.copy(
                lyrics = lyrics
            ).let { return DataResult.Ok(it) }
        }

        val track = musicModel.name?.replace(' ', '+')
        val artist = musicModel.artists?.get(0)?.name?.replace(' ', '+')
        val album = musicModel.album?.name?.replace(' ', '+')
        val duration = musicModel.durationInMs?.div(1000)

        if (track == null || artist == null || album == null || duration == null) return DataResult.Error(
            ServerError("incomplete details from spotify api", null)
        )

        val searchQuery = "track_name=$track&artist_name=$artist&album_name=$album&duration=$duration"

        val response = httpRequestIntoObj<LrcLibJson>("$LRCLIB_API_URL/get?$searchQuery").let {
            when (it) {
                is HttpResponse.Ok -> it.body
                is HttpResponse.Error -> return when (it.statusCode) {
                    404 -> DataResult.Error(UserError("track not found", it.body))
                    else -> DataResult.Error(ServerError("error querying LRCLIB api", it.body))
                }
            }
        }

        val lyrics = Lyrics(
            lrcLibId = response.id,
            instrumental = response.instrumental,
            lyrics = response.syncedLyrics
        ).also {
            cache.put(musicModel.spotifyId, it)
        }

        musicModel.copy(
            lyrics = lyrics
        ).let { return DataResult.Ok(it) }
    }

    companion object {
        private val cache = MemoryCache<String, Lyrics>()
    }
}

