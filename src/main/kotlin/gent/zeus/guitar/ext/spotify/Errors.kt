package gent.zeus.guitar.ext.spotify

import gent.zeus.guitar.ServerError
import gent.zeus.guitar.UserError

class TrackNotFoundError : UserError("track not found", 404)

class SpotifyError : ServerError("unknown error during spotify fetching")
