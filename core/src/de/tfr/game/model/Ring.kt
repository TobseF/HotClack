package de.tfr.game.model


import java.util.*

/**
 * @author Tobse4Git@gmail.com
 */
class Ring(val gameField: GameField, val index: Int) : Iterable<Block> {

    private val SIDES = 12

    override fun iterator() = blocks.iterator()

    private var blocks: Array<Block> = Array(SIDES, { segment -> Block(this, index, segment) })

    operator fun get(orientation: Orientation) = blocks[orientation.ordinal]
    operator fun get(segment: Int) = blocks[segment]

    fun reset() = blocks.forEach { it.reset() }

    fun getTakenCount() = blocks.filter { it.isTaken() }.count()

    fun size() = SIDES

    fun isFull() = getTakenCount() == SIDES

    fun freeSides(): List<Orientation> {
        return emptyList();
        //return blocks.filter { it.isEmpty() }.map { it.orientation }.toList()
    }

    fun randomFreeSide(): Orientation? {
        val freeSides = freeSides()
        if (freeSides.isEmpty()) {
            return null
        }
        return freeSides[Random().nextInt(freeSides.size)]
    }

    override fun toString(): String {
        return "Ring:$index {${blocks.map { it.row.toString() + (if (it.isEmpty()) "[_]" else "[X]") }.joinToString(",")}}"
    }

}