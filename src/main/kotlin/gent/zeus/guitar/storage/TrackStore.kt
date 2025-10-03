package gent.zeus.guitar.storage

import gent.zeus.guitar.data.Track

interface TrackStore {
    fun store(track: Track)
    fun retrieve(id: String): Track?
    fun delete(id: String)
}
