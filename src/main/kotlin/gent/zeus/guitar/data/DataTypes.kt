package gent.zeus.guitar.data

import com.fasterxml.jackson.annotation.JsonInclude

sealed interface MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Track(
    var spotifyId: String,
    var name: String?,
    var album: Album?,
    var artists: List<Artist>?,
    var durationInMs: Int?,
    var imageUrl: String?,
    var spotifyUrl: String?,
    var votesFor: Int?,
    var votesAgainst: Int?,
) : MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Artist(
    var spotifyId: String,
    var name: String?,
    var genres: List<String>?,
    var spotifyUrl: String?
) : MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Album(
    var spotifyId: String,
    var name: String?,
    var tracks: List<Track>?,
    var artists: List<Artist>?,
    var spotifyUrl: String?
) : MusicalObject
