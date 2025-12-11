package gent.zeus.guitar.ext.spotify

import gent.zeus.guitar.ServerError
import gent.zeus.guitar.UserError

class TrackNotFoundError(remoteError: String) : UserError("track not found", remoteError, 404)

class SpotifyError(remoteError: String) : ServerError("unspecified error during spotify fetching", remoteError)
