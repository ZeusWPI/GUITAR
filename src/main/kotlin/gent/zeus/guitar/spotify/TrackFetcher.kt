package gent.zeus.guitar.spotify

import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.Track
import gent.zeus.guitar.votes.VoteFetcher

class TrackFetcher(id: String) : SpotifyFetcher<Track>(id, SpotifyObjectType.TRACK) {
    override fun fetch(): Track? = getSpotifyJson<SpotifyTrackJson>()?.let { trackJson ->
        val voteCount = VoteFetcher().getVotes(trackJson.id)  // TODO: inject this somewhere else
        Track(
            spotifyId = trackJson.id,
            name = trackJson.name,
            album = trackJson.album?.let { albumJson ->
                Album(
                    spotifyId = albumJson.id,
                    name = albumJson.name,
                    tracks = null,
                    artists = albumJson.artists?.map { artistJson ->
                        Artist(
                            spotifyId = artistJson.id,
                            name = artistJson.name,
                            genres = null,
                            spotifyUrl = null,
                        )
                    } ?: emptyList(),
                    spotifyUrl = null,
                )
            },
            artists = trackJson.artists?.map { artistJson ->
                Artist(
                    spotifyId = artistJson.id,
                    name = artistJson.name,
                    genres = null,
                    spotifyUrl = null,
                )
            } ?: emptyList(),
            durationInMs = trackJson.durationMs,
            imageUrl = trackJson.album?.images?.maxBy { imageJson -> imageJson.height }?.url,
            spotifyUrl = trackJson.externalUrls?.spotify,
            votesFor = voteCount.votesFor,
            votesAgainst = voteCount.votesAgainst,
        )
    }
}