package gent.zeus.guitar.db

import gent.zeus.guitar.data.Track

interface TrackStore {
    fun store(track: Track)
    fun retrieve(id: String): Track?
}
