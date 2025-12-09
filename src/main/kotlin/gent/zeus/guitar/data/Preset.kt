package gent.zeus.guitar.data

sealed class Preset(val baseObject: MusicModel) {
    class Track : Preset(gent.zeus.guitar.data.Track())
}
