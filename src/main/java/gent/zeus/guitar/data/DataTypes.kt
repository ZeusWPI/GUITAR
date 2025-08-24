package gent.zeus.guitar.data

import com.fasterxml.jackson.annotation.JsonInclude

interface MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Track(
    val spotifyId: String,
    val name: String?,
    val album: Album?,
    val artists: List<Artist>?,
) : MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Artist(
    val spotifyId: String,
    val name: String?,
    val albums: List<Album>?
) : MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Album(
    val spotifyId: String,
    val name: String?,
    val tracks: List<Track>?,
    val artists: List<Artist>?,
) : MusicalObject
