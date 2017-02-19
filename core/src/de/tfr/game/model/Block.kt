package de.tfr.game.model

/**
 * @author Tobse4Git@gmail.com
 */
class Block(val ring: Ring, var row: Int, var segment: Int) {
    var stone: Stone? = null

    fun isEmpty(): Boolean {
        return stone == null
    }

    fun isTaken(): Boolean {
        return !isEmpty()
    }

    override fun toString() = "Block [$row ${ring.index} $stone]"

    fun reset() {
        stone = null
    }
}