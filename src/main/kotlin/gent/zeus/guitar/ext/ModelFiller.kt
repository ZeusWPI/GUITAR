package gent.zeus.guitar.ext

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.data.MusicModel

interface ModelFiller<T : MusicModel> {

    /**
     * fetch data and put it in the given model
     *
     * this should _copy_ the model, not modify it directly.
     * (use `Model.copy(new values...)`)
     */
    fun fetchInto(musicModel: T): DataResult<T>
}
