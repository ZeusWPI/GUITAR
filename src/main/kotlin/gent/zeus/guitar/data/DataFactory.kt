package gent.zeus.guitar.data

import gent.zeus.guitar.DoubleErrorLists
import gent.zeus.guitar.ext.DataFiller
import gent.zeus.guitar.logErrors
import gent.zeus.guitar.spotify.AlbumFetcher
import gent.zeus.guitar.spotify.ArtistFetcher
import gent.zeus.guitar.spotify.TrackFetcher

class DataFactory {
    fun getTrack(spotifyId: String): Track = Track(spotifyId).also {
        val errors = fillObject(
            it,
            listOf(
                TrackFetcher(spotifyId),
            ),
            emptyList(),
        )
    }

    fun getArtist(spotifyId: String): Artist = Artist(spotifyId).also {
        val errors = fillObject(
            it,
            listOf(
                ArtistFetcher(spotifyId),
            ),
            emptyList(),
        )
    }

    fun getAlbum(spotifyId: String): Album = Album(spotifyId).also {
        val errors = fillObject(
            it,
            listOf(
                AlbumFetcher(spotifyId),
            ),
            emptyList(),
        )
    }

    /**
     * fills the given musical object with data using the given data fillers. will modify the object.
     * @param musicalObject the object to fill
     * @param importantDataFillers data fillers to fill the object with. if one of these gives an error, it will be
     * added to the important errors list
     * @param unimportantDataFillers data fillers to fill the object with. if one of these gives an error, it will be
     * added to the unimportant errors list
     * @param logErrors: log errors in console
     * @return a list of errors that were caused (will be empty if there were no errors)
     */
    private fun <T : MusicalObject> fillObject(
        musicalObject: T,
        importantDataFillers: Collection<DataFiller<T>>,
        unimportantDataFillers: Collection<DataFiller<T>>,
        logErrors: Boolean = true
    ): DoubleErrorLists = DoubleErrorLists(
        important = importantDataFillers
            .mapNotNull { it.fetchInto(musicalObject) }
            .also {
                if (!logErrors) return@also
                it.logErrors("important error fetching data:")
            },
        unimportant = unimportantDataFillers
            .mapNotNull { it.fetchInto(musicalObject) }
            .also {
                if (!logErrors) return@also
                it.logErrors("unimportant error fetching data:")
            },
    )
}
