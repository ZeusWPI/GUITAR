# GUITAR

Global Unification of Information about Tracks And Records

api and mqtt client to get info about music playing in the kelder (and info from Spotify in general)

## features

- an http api to query details about tracks, albums and artists from Spotify, and to get info on what's currently
  playing in the kelder
- broadcasts info on currently playing tracks on the kelder's mqtt broker

more features soon™

## usage

### api

in the kelder, the host is `http://guitar.kelder.local` (or just `http://guitar`)

- `/track/{spotify id}/details`: get details about track
- `/album/{spotify id}/details`: get details about album
- `/artist/{spotify id}/details`: get details about artist
- `/current`: get details about the currently playing track

### mqtt

details about the currently playing track are broadcasted on the mqtt broker (`mqtt://koin:1883`) under the topic
`music/current_song_info`.

---

## ideas

- [ ] lyrics
- [x] album art
- [ ] genres
- [x] current status
- [x] broadcast details on mqtt
- [ ] playlist generation based on stats ("kelder toppers" etc)
