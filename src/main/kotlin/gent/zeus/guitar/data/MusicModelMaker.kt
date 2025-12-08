package gent.zeus.guitar.data

import gent.zeus.guitar.DataFetchError
import gent.zeus.guitar.DataResult
import gent.zeus.guitar.MultiError
import gent.zeus.guitar.ext.DataFiller

abstract class MusicModelMaker<T : MusicModel>(emptyModel: T, vararg val fillers: DataFiller<T>) {
    var model = emptyModel

    /**
     * assembles a musical object with the given id
     * @param id the spotify id of the object
     * @param ignoreErrors whether to return the model anyway if errors where raised (will have missing fields)
     */
    fun getModel(id: String, ignoreErrors: Boolean = false): DataResult<T> {
        val errors = mutableListOf<DataFetchError>()
        fillers.forEach { filler ->
            when (val result = filler.fetchInto(model)) {
                is DataResult.Ok -> model = result.value
                is DataResult.Error<*, *> -> errors.add(result.error)
            }
        }

        if (ignoreErrors) return DataResult.Ok(model)
        return DataResult.Error(MultiError(errors))
    }
}
