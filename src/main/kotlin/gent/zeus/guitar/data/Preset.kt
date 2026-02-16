package gent.zeus.guitar.data

import gent.zeus.guitar.ext.spotify.AlbumFetcher
import gent.zeus.guitar.ext.spotify.ArtistFetcher
import gent.zeus.guitar.ext.spotify.TrackFetcher
import gent.zeus.guitar.ext.votes.VoteFetcher

object Preset {
    object Track {
        val details get() = MusicModelMaker(::Track, TrackFetcher(), VoteFetcher())
        val voteless get() = MusicModelMaker(::Track, TrackFetcher())
    }

    object Album {
        val details get() = MusicModelMaker(::Album, AlbumFetcher())
    }

    object Artist {
        val details get() = MusicModelMaker(::Artist, ArtistFetcher())
    }
}
