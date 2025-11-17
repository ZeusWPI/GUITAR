package gent.zeus.guitar.ext

import gent.zeus.guitar.DataFetchError
import gent.zeus.guitar.DataResult
import gent.zeus.guitar.Logging
import gent.zeus.guitar.SPOTIFY_API_URL
import gent.zeus.guitar.data.MusicalObject
import gent.zeus.guitar.makeAuthorizedRequest
import gent.zeus.guitar.spotify.SpotifyError
import gent.zeus.guitar.spotify.SpotifyJson
import gent.zeus.guitar.spotify.SpotifyToken
import gent.zeus.guitar.spotify.TrackNotFoundError

abstract interface DataFiller<T : MusicalObject> {
    /**
     * fetch data and put it in the given MusicalObject
     */
    fun fetchInto(musicalObject: T): DataFetchError?
}
