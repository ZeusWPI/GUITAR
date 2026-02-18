package gent.zeus.guitar.api

import gent.zeus.guitar.PlayerState
import gent.zeus.guitar.data.Preset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/track/{id}")
class TrackController {

    @GetMapping("/details")
    private fun trackDetails(@PathVariable id: String) = getApiResponse(Preset.Track.details, id)

    @GetMapping("/lyrics")
    private fun trackLyrics(@PathVariable id: String) = getApiResponse(Preset.Track.lyrics, id) { it.lyrics }

    @GetMapping("/all")
    private fun trackAll(@PathVariable id: String) = getApiResponse(Preset.Track.all, id, ignoreErrors = true)
}


@RestController
@RequestMapping("/album/{id}")
class AlbumController {

    @GetMapping("/details")
    private fun albumDetails(@PathVariable id: String) = getApiResponse(Preset.Album.details, id)
}


@RestController
@RequestMapping("/artist/{id}")
class ArtistController {

    @GetMapping("/details")
    private fun artistDetails(@PathVariable id: String) = getApiResponse(Preset.Artist.details, id)
}


@RestController
@RequestMapping("/current")
class CurrentController {

    @GetMapping("")
    private fun currentTrack() = runBlocking {
        val id = PlayerState.mutex.withLock { PlayerState.currentTrackId }

        if (id != null)
            getApiResponse(Preset.Track.detailsWithState, id)
        else
            ResponseEntity.status(200).body(null)
    }
}
