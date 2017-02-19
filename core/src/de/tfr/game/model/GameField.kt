package de.tfr.game.model

/**
 * @author Tobse4Git@gmail.com
 */
class GameField(numberOfRings: Int, private val numberOfSegments: Int) : Iterable<Ring> {

    private var rings: Array<Ring> = Array(numberOfRings, this::newRing)
    val player: Player

    init {
        player = Player(Block(rings[0], 0, 0), Stone.Color.Red)
    }

    override fun iterator() = rings.iterator()


    private fun newRing(index: Int) = Ring(this, index)

    operator fun get(ringIndex: Int) = rings[ringIndex]

    fun reset() = rings.forEach(Ring::reset)

    fun getNumberOfRings() = rings.size

    fun getNumberOfSegments() = numberOfSegments

}