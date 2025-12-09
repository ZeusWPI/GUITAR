package gent.zeus.guitar.api

import gent.zeus.guitar.DataResult
import gent.zeus.guitar.data.MusicModel
import org.springframework.http.ResponseEntity

interface ApiResponseObj

private data class ApiError(
    val message: String,
    val status: Int,
) : ApiResponseObj

abstract class ApiResponse<T : MusicModel> {
    var ignoreErrors = false

    protected abstract fun collect(id: String): DataResult<T>

    fun get(id: String): ResponseEntity<ApiResponseObj> = when (val it = collect(id)) {
        is DataResult.Ok -> ResponseEntity.status(200).body(it.value)
        is DataResult.Error<*, *> -> ResponseEntity.status(it.error.httpStatusCode).body(
            ApiError(
                message = it.error.message,
                status = it.error.httpStatusCode,
            )
        )
    }
}
