package gent.zeus.guitar.spotify

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import gent.zeus.guitar.Global
import gent.zeus.guitar.Logging
import org.springframework.http.MediaType
import org.springframework.web.client.body

internal class ApiTokenGetterOld {
    // xhs post
    // https://accounts.spotify.com/api/token
    // content-type:application/x-www-form-urlencoded
    // --raw "

    // TODO implement caching

    val spotifyId: String = System.getenv("SPOTIFY_CLIENT_ID");
    val spotifySecret: String = System.getenv("SPOTIFY_CLIENT_SECRET");

    val token: String?
        get() {
            val accessToken: SpotifyAccessToken? = Global.REST_CLIENT.post()
                .uri("https://accounts.spotify.com/api/token")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("grant_type=client_credentials&client_id=${spotifyId}&client_secret=${spotifySecret}")
                .retrieve()
                .body<SpotifyAccessToken>()

            if (accessToken == null) {
                throw SpotifyApiException("response body was empty")
            }
            Logging.log.info("got spotify token: ${accessToken.token}")
            return accessToken.token
        }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    internal data class SpotifyAccessToken(
        @JsonProperty("access_token") val token: String?,
        val tokenType: String?,
        val expiresIn: Int
    )
}
