package gent.zeus.guitar.storage

import gent.zeus.guitar.data.Track
import gent.zeus.guitar.logger


private val trackStorage: MutableMap<String, Track> = mutableMapOf()


class InMemoryTrackStore : TrackStore {
    override fun store(track: Track) {
        trackStorage[track.spotifyId] = track
        logger.info("added track to memory db: ${track.spotifyId}")
    }

    override fun retrieve(id: String): Track? = trackStorage[id]?.also {
        logger.info("retrieved track from in memory db: ${it.spotifyId}")
    }

    override fun delete(id: String) {
        trackStorage.remove(id)
    }
}
