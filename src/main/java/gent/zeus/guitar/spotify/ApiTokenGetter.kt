package gent.zeus.guitar.spotify

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

class ApiTokenGetter {
    val token: String?
        get() {
            val accessToken: SpotifyAccessToken? = REST_CLIENT.get()
                .uri("https://accounts.spotify.com/api/token")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body<SpotifyAccessToken?>(SpotifyAccessToken::class.java)

            if (accessToken == null) {
                throw SpotifyApiException("response body was empty")
            }
            return accessToken.token
        }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    internal data class SpotifyAccessToken(
        @JsonProperty("access_token") val token: String?,
        val tokenType: String?,
        val expiresIn: Int
    )

    companion object {
        // xhs post
        // https://accounts.spotify.com/api/token
        // content-type:application/x-www-form-urlencoded
        // --raw "grant_type=client_credentials&client_id=$SPOTI_ID&client_secret=$SPOTI_SECRET
        // TODO implement caching
        private val REST_CLIENT: RestClient = RestClient.create()
    }
}
