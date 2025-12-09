package gent.zeus.guitar.api

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.data.MusicModel
import gent.zeus.guitar.data.MusicModelMaker
import org.springframework.http.ResponseEntity

interface ApiResponseObj

private data class ApiError(
    val message: String,
    val status: Int,
) : ApiResponseObj

fun <T : MusicModel> getApiResponse(
    modelMaker: MusicModelMaker<T>,
    id: String,
    ignoreErrors: Boolean = false
): ResponseEntity<ApiResponseObj> = when (val it = modelMaker.getModel(id, ignoreErrors)) {
    is DataResult.Ok -> ResponseEntity.status(200).body(it.value)
    is DataResult.Error<*, *> -> ResponseEntity.status(it.error.httpStatusCode).body(
        ApiError(
            message = it.error.message,
            status = it.error.httpStatusCode,
        )
    )
}
