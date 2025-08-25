package gent.zeus.guitar.api

import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.DataProvider
import gent.zeus.guitar.data.Track
import gent.zeus.guitar.spotify.AlbumFetcher
import gent.zeus.guitar.spotify.ArtistFetcher
import gent.zeus.guitar.spotify.TrackFetcher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/track/{id}")
class TrackController(val dataProvider: DataProvider) {

    @GetMapping("/details")
    fun getTrackById(@PathVariable id: String): Track? = dataProvider.getTrack(id)
}


@RestController
@RequestMapping("/album/{id}")
class AlbumController(val dataProvider: DataProvider) {

    @GetMapping("/details")
    fun getTrackById(@PathVariable id: String): Album? = dataProvider.getAlbum(id)
}


@RestController
@RequestMapping("/artist/{id}")
class ArtistController(val dataProvider: DataProvider) {

    @GetMapping("/details")
    fun getTrackById(@PathVariable id: String): Artist? = dataProvider.getArtist(id)
}
