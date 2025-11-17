package gent.zeus.guitar.ext

import gent.zeus.guitar.DataFetchError
import gent.zeus.guitar.data.MusicalObject

interface DataFiller<T : MusicalObject> {
    /**
     * fetch data and put it in the given MusicalObject
     */
    fun fetchInto(musicalObject: T): DataFetchError?
}
