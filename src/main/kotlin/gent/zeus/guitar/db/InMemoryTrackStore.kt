package gent.zeus.guitar.db

import gent.zeus.guitar.Logging
import gent.zeus.guitar.data.Track
import org.springframework.stereotype.Component


private val trackStorage: MutableMap<String, Track> = mutableMapOf()


@Component
class InMemoryTrackStore : TrackStore {
    override fun store(track: Track) {
        trackStorage[track.spotifyId] = track
    }

    override fun retrieve(id: String): Track? = trackStorage[id]?.also {
        Logging.log.info("retrieved track from in memory db: ${it.spotifyId}")
    }
}
