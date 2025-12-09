package gent.zeus.guitar.data

import gent.zeus.guitar.ext.spotify.TrackFetcher

object Preset {
    object Track {
        val details get() = MusicModelMaker({ id -> Track(id) }, TrackFetcher())
    }
}
