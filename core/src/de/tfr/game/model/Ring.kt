package de.tfr.game.model


/**
 * @author Tobse4Git@gmail.com
 */
class Ring(val gameField: GameField, val index: Int) : Iterable<Block> {

    private val segments = 12

    override fun iterator() = blocks.iterator()

    private var blocks: Array<Block> = Array(segments, { segment -> Block(this, index, segment) })

    operator fun get(segment: Int) = blocks[segment]

    fun reset() = blocks.forEach { it.reset() }

    fun getTakenCount() = blocks.filter { it.isTaken() }.count()

    fun size() = segments

    fun isFull() = getTakenCount() == segments


    override fun toString(): String {
        return "Ring:$index {${blocks.map { it.row.toString() + (if (it.isEmpty()) "[_]" else "[X]") }.joinToString(",")}}"
    }

}