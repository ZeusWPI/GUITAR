package gent.zeus.guitar

/**
 * returns `true` if none of the values are `null`, `false` otherwise
 */
fun testNotNull(vararg values: Any?) = values.filterNotNull().isEmpty()

fun <T> Iterable<T>.returnIf( predicate: (T) -> Boolean ) =
