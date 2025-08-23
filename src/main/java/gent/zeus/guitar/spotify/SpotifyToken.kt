package gent.zeus.guitar.spotify

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import gent.zeus.guitar.Logging
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.StartupCheck
import gent.zeus.guitar.StartupCheckResult
import org.springframework.http.MediaType
import org.springframework.web.client.body

/*

    val accessToken: SpotifyAccessToken? = Global.REST_CLIENT.post()
        .uri("https://accounts.spotify.com/api/token")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body("grant_type=client_credentials&client_id=${spotifyId}&client_secret=${spotifySecret}")
        .retrieve()
        .body<SpotifyAccessToken>()


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    internal data class SpotifyAccessToken(
        @JsonProperty("access_token") val token: String?,
        val tokenType: String?,
        val expiresIn: Int
    )
 */

object SpotifyToken : StartupCheck {
    private val spotifyId = System.getenv("SPOTIFY_CLIENT_ID")
    private val spotifySecret = System.getenv("SPOTIFY_CLIENT_SECRET")

    fun get(): String? = requestToken().let {
        if (it == null) {
            Logging.log.error("could not acquire spotify api token!")
        }
        it?.token
    }

    private fun requestToken(): SpotifyAccessToken? =
        REST_CLIENT.post()
            .uri("https://accounts.spotify.com/api/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body("grant_type=client_credentials&client_id=${spotifyId}&client_secret=${spotifySecret}")
            .retrieve()
            .body<SpotifyAccessToken>()

    override fun checkOnStartup(): StartupCheckResult {
        val result = spotifyId != null && spotifySecret != null
        return StartupCheckResult(
            result,
            "SPOTIFY_CLIENT_ID or SPOTIFY_CLIENT_SECRET environment variable not set!".takeUnless { result }
        )
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    private data class SpotifyAccessToken(
        @JsonProperty("access_token") val token: String?,
        val tokenType: String,
        val expiresIn: Int,
    )
}