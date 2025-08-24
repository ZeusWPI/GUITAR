package gent.zeus.guitar.spotify

import gent.zeus.guitar.data.Album
import org.springframework.web.client.body

class AlbumFetcher(id: String) : SpotifyFetcher<Album>(id, SpotifyObjectType.ALBUM) {
    override fun fetch(): Album? = makeApiRequest().body<SpotifyAlbumJson>()
        .takeIfNotNullOrLog()?.let { albumJson ->
            Album(
                albumJson.id,
                albumJson.name,
            )
        }
}
