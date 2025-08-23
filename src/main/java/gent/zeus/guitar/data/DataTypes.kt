package gent.zeus.guitar.data

interface MusicalObject

data class Track(
    val spotifyId: String,
    val name: String?,
    val album: Album?,
    val artists: List<Artist?>,
) : MusicalObject

data class Artist(
    val spotifyId: String,
    val name: String?,
) : MusicalObject

data class Album(
    val spotifyId: String,
    val name: String?,
) : MusicalObject
