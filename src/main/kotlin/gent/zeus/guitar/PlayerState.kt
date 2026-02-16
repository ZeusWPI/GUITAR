package gent.zeus.guitar

import kotlinx.coroutines.sync.Mutex

/**
 * current state of the kelder music. must be accessed using a mutex because it can get accessed concurrently.
 */
object PlayerState {
    /**
     * spotify id of current track
     */
    var currentTrackId: String? = null

    /**
     * unix start time (with millisecond precision) of current track, _assuming that is has been playing
     * the whole time (not paused)_.
     *
     * e.g. if a track gets paused for 5 seconds, this value will increase by 5000.
     */
    var currentStartTime: Long? = null

    val mutex = Mutex()
}
