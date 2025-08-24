package gent.zeus.guitar.spotify

class SpotifyApiException : RuntimeException {
    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
