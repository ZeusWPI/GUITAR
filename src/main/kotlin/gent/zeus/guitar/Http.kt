package gent.zeus.guitar

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

val REST_CLIENT: RestClient = RestClient.create()

inline fun <reified T : Any> makeAuthorizedRequest(uri: String, bearerToken: String): ResponseEntity<T> =
    REST_CLIENT.get()
        .uri(uri)
        .header(HttpHeaders.AUTHORIZATION, "Bearer $bearerToken")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntity<T>()
