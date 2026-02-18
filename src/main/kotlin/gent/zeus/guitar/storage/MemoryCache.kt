package gent.zeus.guitar.storage

import gent.zeus.guitar.ext.spotify.SpotifyFetcher
import gent.zeus.guitar.ext.spotify.SpotifyJson
import gent.zeus.guitar.logger
import java.util.TreeMap

/**
 * in-memory cache
 *
 * @param K key type
 * @param V value type
 */
class MemoryCache<K, V> {
    private val cache: MutableMap<K, V> = TreeMap()

    /**
     * put item with `id` in the cache
     */
    fun put(key: K, value: V) {
        cache[key] = value
        logger.debug("wrote item to cache: {}", value)
    }

    /**
     * retrieve item with `id` from the cache
     */
    fun get(key: K): V? = cache[key].also {
        if (it != null) logger.debug("retrieved item from cache: {}", key)
    }
}