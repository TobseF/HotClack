package de.tfr.game.util.extensions

/**
 * @author Tobse4Git@gmail.com
 */

/**
 * Iterate over this enum value. Iterating the last one, will return the first.
 */
inline fun <reified T : Enum<T>> T.next(): T {
    val values = enumValues<T>()
    val nextOrdinal = (ordinal + 1) % values.size
    return values[nextOrdinal]
}

/**
 * Iterate over this enum value backwards. Iterating the first one, will return the last.
 */
inline fun <reified T : Enum<T>> T.prev(): T {
    val values = enumValues<T>()
    val nextOrdinal = if ((ordinal - 1) < 0) (values.size - 1) else (ordinal - 1)
    return values[nextOrdinal]
}