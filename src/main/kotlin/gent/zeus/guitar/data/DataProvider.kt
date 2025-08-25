package gent.zeus.guitar.data

import gent.zeus.guitar.db.TrackStore
import gent.zeus.guitar.spotify.AlbumFetcher
import gent.zeus.guitar.spotify.ArtistFetcher
import gent.zeus.guitar.spotify.TrackFetcher
import org.springframework.stereotype.Component

@Component
class DataProvider(val trackStore: TrackStore) {
    fun getTrack(id: String): Track? = trackStore.retrieve(id) ?: TrackFetcher(id).fetch()

    fun getAlbum(id: String): Album? = AlbumFetcher(id).fetch()

    fun getArtist(id: String): Artist? = ArtistFetcher(id).fetch()
}
