package gent.zeus.guitar.data

import gent.zeus.guitar.data.Preset.Track
import gent.zeus.guitar.ext.lyrics.LyricsFetcher
import gent.zeus.guitar.ext.playerstate.PlayerStateFetcher
import gent.zeus.guitar.ext.spotify.AlbumFetcher
import gent.zeus.guitar.ext.spotify.ArtistFetcher
import gent.zeus.guitar.ext.spotify.TrackFetcher
import gent.zeus.guitar.ext.votes.VoteFetcher

object Preset {
    object Track {
        val details get() = MusicModelMaker(::Track, TrackFetcher(), VoteFetcher())
        val voteless get() = MusicModelMaker(::Track, TrackFetcher())
        val detailsWithState get() = MusicModelMaker(::Track, TrackFetcher(), VoteFetcher(), PlayerStateFetcher())
        val lyrics get() = MusicModelMaker(::Lyrics, LyricsFetcher())
    }

    object Album {
        val details get() = MusicModelMaker(::Album, AlbumFetcher())
    }

    object Artist {
        val details get() = MusicModelMaker(::Artist, ArtistFetcher())
    }
}
