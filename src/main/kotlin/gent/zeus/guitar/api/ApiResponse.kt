package gent.zeus.guitar.api

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.data.MusicModel
import gent.zeus.guitar.data.MusicModelMaker
import org.springframework.http.ResponseEntity

private data class ApiError(
    val message: String,
    val status: Int,
)

/**
 * get response to return from the api
 *
 * @param modelMaker model maker to make the model
 * @param id spotify id to make a model from
 * @param ignoreErrors whether to return a successful response anyway when errors where encountered
 * @param extract function to extract a value from the model to return from the api (instead of the whole model)
 */
fun <T : MusicModel> getApiResponse(
    modelMaker: MusicModelMaker<T>,
    id: String,
    ignoreErrors: Boolean = false,
    extract: (T) -> Any?
): ResponseEntity<Any> = when (val it = modelMaker.getModel(id, ignoreErrors)) {
    is DataResult.Ok -> ResponseEntity.status(200).body(extract(it.value))
    is DataResult.Error<*> -> ResponseEntity.status(it.error.httpStatusCode).body(
        ApiError(
            message = it.error.message,
            status = it.error.httpStatusCode,
        )
    )
}

/**
 * get response to return from the api
 *
 * @param modelMaker model maker to make the model
 * @param id spotify id to make a model from
 * @param ignoreErrors whether to return a successful response anyway when errors where encountered
 */
fun <T : MusicModel> getApiResponse(
    modelMaker: MusicModelMaker<T>,
    id: String,
    ignoreErrors: Boolean = false,
) = getApiResponse(modelMaker, id, ignoreErrors) { it }
