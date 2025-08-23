package gent.zeus.guitar.api

import gent.zeus.guitar.data.Track
import gent.zeus.guitar.spotify.TrackFetcher
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/track/{id}")
class TrackController {

    @GetMapping("/details")
    fun getTrackById(@PathVariable id: String): Track? = TrackFetcher(id).fetchDetails()
}
