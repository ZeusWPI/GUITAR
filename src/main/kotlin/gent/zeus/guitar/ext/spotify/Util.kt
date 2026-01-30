package gent.zeus.guitar.ext.spotify

import com.github.f4b6a3.uuid.codec.base.Base62Codec


/**
 * check whether string is valid base62
 */
fun checkBase62(string: String): Boolean {
    // https://stackoverflow.com/a/68913412
    try {
        Base62Codec.INSTANCE.decode(string)
        return true
    } catch (e: Exception) {
        return false
    }
}
