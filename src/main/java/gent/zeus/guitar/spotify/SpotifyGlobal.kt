package gent.zeus.guitar.spotify

object SpotifyGlobal {
    private val apiTokenGetter = ApiTokenGetterOld()

    val token: String?
        get() = apiTokenGetter.token
}
