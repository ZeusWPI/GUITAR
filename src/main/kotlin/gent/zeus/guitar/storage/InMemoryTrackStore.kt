package gent.zeus.guitar.storage

import gent.zeus.guitar.Logging
import gent.zeus.guitar.data.Track


private val trackStorage: MutableMap<String, Track> = mutableMapOf()


class InMemoryTrackStore : TrackStore {
    override fun store(track: Track) {
        trackStorage[track.spotifyId] = track
        Logging.log.info("added track to memory db: ${track.spotifyId}")
    }

    override fun retrieve(id: String): Track? = trackStorage[id]?.also {
        Logging.log.info("retrieved track from in memory db: ${it.spotifyId}")
    }
}
