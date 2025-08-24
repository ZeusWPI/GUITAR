package gent.zeus.guitar.api

import gent.zeus.guitar.data.Album
import gent.zeus.guitar.spotify.AlbumFetcher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/album/{id}")
class AlbumController {

    @GetMapping("/details")
    fun getTrackById(@PathVariable id: String): Album? = AlbumFetcher(id).fetch()
}
