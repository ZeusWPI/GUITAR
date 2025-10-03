package gent.zeus.guitar.data

import com.fasterxml.jackson.annotation.JsonInclude

sealed interface MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Track(
    val spotifyId: String,
    val name: String?,
    val album: Album?,
    val artists: List<Artist>?,
    val durationInMs: Int?,
    val imageUrl: String?,
    val spotifyUrl: String?,
    val votesFor: Int?,
    val votesAgainst: Int?,
) : MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Artist(
    val spotifyId: String,
    val name: String?,
    val genres: List<String>?,
    val spotifyUrl: String?
) : MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Album(
    val spotifyId: String,
    val name: String?,
    val tracks: List<Track>?,
    val artists: List<Artist>?,
    val spotifyUrl: String?
) : MusicalObject
