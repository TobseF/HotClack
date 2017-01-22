package de.tfr.game.lib

import com.badlogic.gdx.math.MathUtils

/**
 * @author Tobse4Git@gmail.com
 */


fun <T> List<T>.random(): T? {
    if (this.isEmpty()) {
        return null
    }
    return this[MathUtils.random(Math.max(0, this.size - 1))]
}

fun <T> Array<T>.random(): T {
    return this[MathUtils.random(Math.max(0, this.size - 1))]
}
