package gent.zeus.guitar.data

data class Track(
    val spotifyId: String,
    val name: String?,
    val album: Album?,
    val artists: List<Artist?>,
)

data class Artist(
    val spotifyId: String,
    val name: String?,
)

data class Album(
    val spotifyId: String,
    val name: String?,
)
