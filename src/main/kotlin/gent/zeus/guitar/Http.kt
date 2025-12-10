package gent.zeus.guitar

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import org.springframework.web.client.toEntity

val REST_CLIENT: RestClient = RestClient.create()

inline fun <reified T : Any> httpRequestIntoObj(uri: String, bearerToken: String? = null): HttpResponse<T> =
    try {
        REST_CLIENT.get()
            .uri(uri)
            .let {
                bearerToken ?: return@let it
                it.header(HttpHeaders.AUTHORIZATION, "Bearer $bearerToken")
            }
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body<T>().let {
                return HttpResponse.Ok(200, it)
            }
    } catch (e: HttpClientErrorException) {
        HttpResponse.Error(e.statusCode.value(), e.responseBodyAsString)
    }

sealed class HttpResponse<out T> {
    data class Ok<T>(val statusCode: Int, val body: T?) : HttpResponse<T>()
    data class Error(val statusCode: Int, val body: String) : HttpResponse<Nothing>()
}
