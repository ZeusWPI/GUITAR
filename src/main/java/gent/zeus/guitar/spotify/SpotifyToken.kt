package gent.zeus.guitar.spotify

import gent.zeus.guitar.StartupCheck
import gent.zeus.guitar.StartupCheckResult

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

    fun get(): String {
        TODO()
    }

    private fun requestToken(): String {
        TODO()
    }

    override fun checkOnStartup(): StartupCheckResult {
        val result = spotifyId != null && spotifySecret != null
        return StartupCheckResult(
            result,
            "SPOTIFY_CLIENT_ID or SPOTIFY_CLIENT_SECRET environment variable not set!".takeIf { !result }
        )
    }
}