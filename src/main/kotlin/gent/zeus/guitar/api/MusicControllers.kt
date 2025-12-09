package gent.zeus.guitar.api

import gent.zeus.guitar.data.Album
import gent.zeus.guitar.data.Artist
import gent.zeus.guitar.data.DataProvider
import gent.zeus.guitar.data.Preset
import gent.zeus.guitar.data.Track
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/track/{id}")
class TrackController() {

    @GetMapping("/details")
    private fun trackDetails(@PathVariable id: String) = getApiResponse(Preset.Track.details, id)

    @GetMapping("/test")
    fun test(@PathVariable id: String): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(":(")
}


@RestController
@RequestMapping("/album/{id}")
class AlbumController() {

    @GetMapping("/details")
    private fun albumDetails(@PathVariable id: String) = getApiResponse(Preset.Album.details, id)
}


@RestController
@RequestMapping("/artist/{id}")
class ArtistController() {

    @GetMapping("/details")
    private fun artistDetails(@PathVariable id: String) = getApiResponse(Preset.Artist.details, id)
}
