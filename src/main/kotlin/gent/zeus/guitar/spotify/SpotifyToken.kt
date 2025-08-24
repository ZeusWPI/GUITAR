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


object SpotifyToken : StartupCheck {
    private val spotifyId = System.getenv("SPOTIFY_CLIENT_ID")
    private val spotifySecret = System.getenv("SPOTIFY_CLIENT_SECRET")

    private var tokenCache: TokenCache = TokenCache("", 0)

    fun get(): String? = tokenCache.token ?: requestToken()
        .also { tokenCache = TokenCache(it?.token ?: "", it?.expiresIn ?: 0) }
        ?.token


    private fun requestToken(): SpotifyAccessToken? = REST_CLIENT.post()
        .uri("https://accounts.spotify.com/api/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body("grant_type=client_credentials&client_id=${spotifyId}&client_secret=${spotifySecret}")
        .retrieve()
        .body<SpotifyAccessToken>()
        .also {
            if (it == null) {
                Logging.log.error("could not acquire spotify api token!")
            } else {
                Logging.log.info("got new spotify api token, valid for ${it.expiresIn} seconds")
            }
        }

    override fun checkOnStartup(): StartupCheckResult {
        val result = spotifyId != null && spotifySecret != null
        return StartupCheckResult(
            result,
            "SPOTIFY_CLIENT_ID or SPOTIFY_CLIENT_SECRET environment variable not set!".takeUnless { result }
        )
    }

    private class TokenCache(token: String, validForSeconds: Int) {
        val expiryTime = System.currentTimeMillis() / 1000 + validForSeconds
        private val _token = token
        val token: String?
            get() = if (System.currentTimeMillis() / 1000 > expiryTime - 60) {
                Logging.log.info("spotify token cache expired")
                null
            } else {
                _token
            }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    private data class SpotifyAccessToken(
        @JsonProperty("access_token") val token: String?,
        val tokenType: String,
        val expiresIn: Int,
    )
}