package gent.zeus.guitar.data

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.DoubleErrorLists
import gent.zeus.guitar.ext.DataFiller
import gent.zeus.guitar.logErrors

class MusicModelMaker<T : MusicModel>(
) {

    /**
     * assembles a musical object with the given id
     * @param id the spotify id of the object
     */
    fun getModel(id: String): DataResult<T>

    /**
     * fills the given musical object with data using the given data fillers. will modify the object.
     * @param musicalObject the object to fill
     * @param importantDataFillers data fillers to fill the object with. if one of these gives an error, it will be
     * added to the important errors list
     * @param unimportantDataFillers data fillers to fill the object with. if one of these gives an error, it will be
     * added to the unimportant errors list
     * @param logErrors log errors in console
     * @return a list of errors that were caused (will be empty if there were no errors)
     */
    protected fun <T : MusicModel> fillObject(
        musicalObject: T,
        importantDataFillers: Collection<DataFiller<T>>,
        unimportantDataFillers: Collection<DataFiller<T>>,
    ): DoubleErrorLists = DoubleErrorLists(
        important = importantDataFillers.mapNotNull { it.fetchInto(musicalObject) },
        unimportant = unimportantDataFillers.mapNotNull { it.fetchInto(musicalObject) },
    )

    protected fun <T : MusicModel> assemble(
        importantDataFillers: Iterable<DataFiller<T>>,
        unimportantDataFillers: Iterable<DataFiller<T>>,
    ):
}
