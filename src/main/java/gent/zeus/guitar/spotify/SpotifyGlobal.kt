package gent.zeus.guitar.spotify

object SpotifyGlobal {
    private val apiTokenGetter = ApiTokenGetter()

    val token: String?
        get() = apiTokenGetter.token
}
