package gent.zeus.guitar.ext.spotify

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import gent.zeus.guitar.Environment
import gent.zeus.guitar.REST_CLIENT
import gent.zeus.guitar.logger
import org.springframework.http.MediaType
import org.springframework.web.client.body


object SpotifyToken {
    private val spotifyId = Environment.SPOTIFY_CLIENT_ID
    private val spotifySecret = Environment.SPOTIFY_CLIENT_SECRET

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
                logger.error("could not acquire spotify api token!")
            } else {
                logger.info("got new spotify api token, valid for ${it.expiresIn} seconds")
            }
        }

    private class TokenCache(token: String, validForSeconds: Int) {
        val expiryTime = System.currentTimeMillis() / 1000 + validForSeconds
        private val _token = token
        val token: String?
            get() = if (System.currentTimeMillis() / 1000 > expiryTime - 60) {
                logger.info("spotify token cache expired")
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