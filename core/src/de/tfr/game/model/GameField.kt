package de.tfr.game.model

/**
 * @author Tobse4Git@gmail.com
 */
class GameField(val size: Int) : Iterable<Ring> {

    private var rings: Array<Ring> = Array(size, this::newRing)
    val player: Stone

    init {
        player = Stone(this[0][0])
    }

    override fun iterator() = rings.iterator()


    private fun newRing(index: Int) = Ring(this, index)

    operator fun get(index: Int) = rings[index]

    fun reset() = rings.forEach(Ring::reset)

    fun segments() = rings[0].size()

}