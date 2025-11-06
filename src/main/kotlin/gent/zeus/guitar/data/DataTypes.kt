package gent.zeus.guitar.data

import com.fasterxml.jackson.annotation.JsonInclude

sealed interface MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Track(
    var spotifyId: String,
    var name: String? = null,
    var album: Album? = null,
    var artists: List<Artist>? = null,
    var durationInMs: Int? = null,
    var imageUrl: String? = null,
    var spotifyUrl: String? = null,
    var votesFor: Int? = null,
    var votesAgainst: Int? = null,
) : MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Artist(
    var spotifyId: String,
    var name: String? = null,
    var genres: List<String>? = null,
    var spotifyUrl: String? = null
) : MusicalObject

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Album(
    var spotifyId: String,
    var name: String? = null,
    var tracks: List<Track>? = null,
    var artists: List<Artist>? = null,
    var spotifyUrl: String?
) : MusicalObject
