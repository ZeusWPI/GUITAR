package gent.zeus.guitar.ext.playerstate

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.EmptyPlayerStateError
import gent.zeus.guitar.PlayerState
import gent.zeus.guitar.ServerError
import gent.zeus.guitar.UserError
import gent.zeus.guitar.data.Track
import gent.zeus.guitar.ext.ModelFiller
import gent.zeus.guitar.ext.OrderingError
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import org.apache.catalina.Server

class PlayerStateFetcher : ModelFiller<Track> {
    override fun fetchInto(musicModel: Track): DataResult<Track> = runBlocking {
        musicModel.durationInMs
            ?: return@runBlocking DataResult.Error(OrderingError("durationInMs"))

        val id = PlayerState.mutex.withLock { PlayerState.currentTrackId }
            ?: return@runBlocking DataResult.Error(EmptyPlayerStateError())

        if (id != musicModel.spotifyId) DataResult.Error(NotCurrentTrackError())

        val startedAt = PlayerState.mutex.withLock { PlayerState.currentStartTime }
            ?: return@runBlocking DataResult.Error(EmptyPlayerStateError())

        DataResult.Ok(
            musicModel.copy(
                startedAtMs = startedAt,
                endsAtMs = startedAt + musicModel.durationInMs
            )
        )
    }
}

class NotCurrentTrackError : ServerError(
    "this is not the current track",
    null,
)
