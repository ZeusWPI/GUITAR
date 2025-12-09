package gent.zeus.guitar.data

import gent.zeus.guitar.DataFetchError
import gent.zeus.guitar.DataResult
import gent.zeus.guitar.MultiError
import gent.zeus.guitar.ext.DataFiller

abstract class MusicModelMaker<T : MusicModel>(var baseModel: T, vararg val fillers: DataFiller<T>) {

    /**
     * assembles a musical object with the given id
     * @param id the spotify id of the object
     * @param ignoreErrors whether to return the model anyway if errors where raised (will have missing fields)
     */
    fun getModel(id: String, ignoreErrors: Boolean = false): DataResult<T> {
        val errors = mutableListOf<DataFetchError>()
        fillers.forEach { filler ->
            when (val result = filler.fetchInto(baseModel)) {
                is DataResult.Ok -> baseModel = result.value
                is DataResult.Error<*, *> -> errors.add(result.error)
            }
        }

        if (ignoreErrors || errors.isEmpty()) return DataResult.Ok(baseModel)
        return DataResult.Error(MultiError(errors))
    }
}
