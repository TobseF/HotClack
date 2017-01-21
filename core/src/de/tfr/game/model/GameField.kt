package de.tfr.game.model

/**
 * @author Tobse4Git@gmail.com
 */
class GameField(val size: Int) : Iterable<Ring> {

    override fun iterator() = rings.iterator()

    private var rings: Array<Ring> = Array(size, this::newRing)

    private fun newRing(index: Int) = Ring(this, index)

    operator fun get(index: Int) = rings[index]

    fun reset() = rings.forEach(Ring::reset)

}