package gent.zeus.guitar.data

import gent.zeus.guitar.storage.InMemoryTrackStore
import gent.zeus.guitar.storage.TrackStore
import gent.zeus.guitar.spotify.AlbumFetcher
import gent.zeus.guitar.spotify.ArtistFetcher
import gent.zeus.guitar.spotify.TrackFetcher
import org.springframework.stereotype.Component

@Component
class DataProvider {
    val dataFactory = DataFactory()

    // TODO: don't implement this as spring component
    val trackStore: TrackStore = InMemoryTrackStore()

    fun getTrack(id: String): Track =
        trackStore.retrieve(id)
            ?: dataFactory.getTrack(id)

    fun removeTrack(id: String) {
        trackStore.delete(id)
    }

//    fun getAlbum(id: String): Album? = AlbumFetcher(id).fetch()

//    fun getArtist(id: String): Artist? = ArtistFetcher(id).fetch()
}
