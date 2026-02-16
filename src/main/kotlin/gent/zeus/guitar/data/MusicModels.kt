package gent.zeus.guitar.data

import com.fasterxml.jackson.annotation.JsonInclude
import gent.zeus.guitar.api.ApiResponseObj

sealed interface MusicModel : ApiResponseObj

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Track(
    val spotifyId: String,
    val name: String? = null,
    val album: Album? = null,
    val artists: List<Artist>? = null,
    val durationInMs: Int? = null,
    val startedAtMs: Long? = null,
    val endsAtMs: Long? = null,
    val imageUrl: String? = null,
    val spotifyUrl: String? = null,
    val votesFor: Int? = null,
    val votesAgainst: Int? = null,
) : MusicModel

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Artist(
    val spotifyId: String,
    val name: String? = null,
    val genres: List<String>? = null,
    val spotifyUrl: String? = null
) : MusicModel

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Album(
    val spotifyId: String,
    val name: String? = null,
    val tracks: List<Track>? = null,
    val artists: List<Artist>? = null,
    val spotifyUrl: String? = null,
) : MusicModel
