package gent.zeus.guitar.ext.lyrics

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.HttpResponse
import gent.zeus.guitar.LRCLIB_API_URL
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.ServerError
import gent.zeus.guitar.UserError
import gent.zeus.guitar.data.Lyrics
import gent.zeus.guitar.data.Preset
import gent.zeus.guitar.ext.ModelFiller
import gent.zeus.guitar.httpRequestIntoObj

class LyricsFetcher : ModelFiller<Lyrics> {
    override fun fetchInto(musicModel: Lyrics): DataResult<Lyrics> {
        val trackDetails = when (val it = Preset.Track.voteless.getModel(musicModel.spotifyId)) {
            is DataResult.Ok -> it.value
            is DataResult.Error<*> -> return it
        }

        val track = trackDetails.name?.replace(' ', '+')
        val artist = trackDetails.artists?.get(0)?.name?.replace(' ', '+')
        val album = trackDetails.album?.name?.replace(' ', '+')
        val duration = trackDetails.durationInMs?.div(1000)

        if (track == null || artist == null || album == null || duration == null) return DataResult.Error(
            ServerError("incomplete details from spotify api", null)
        )

        val searchQuery = "track_name=$track&artist_name=$artist&album_name=$album&duration=$duration"

        val response = httpRequestIntoObj<LrcLibJson>("$LRCLIB_API_URL/search?q=$searchQuery").let {
            when (it) {
                is HttpResponse.Ok -> it.body
                is HttpResponse.Error -> return when (it.statusCode) {
                    404 -> DataResult.Error(UserError("track not found", it.body))
                    else -> DataResult.Error(ServerError("error querying LRCLIB api", it.body))
                }
            }
        }

        musicModel.copy(
            lrcLibId = response.id,
            instrumental = response.instrumental,
            lyrics = response.syncedLyrics
        ).let { return DataResult.Ok(it) }
    }
}

