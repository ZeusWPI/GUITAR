package gent.zeus.guitar.ext

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.data.MusicModel

interface DataFiller<T : MusicModel> {

    /**
     * fetch data and put it in the given MusicalObject
     */
    fun fetchInto(musicModel: T): DataResult<T>
}
