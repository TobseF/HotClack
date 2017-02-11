package de.tfr.game.model


/**
 * @author Tobse4Git@gmail.com
 */
class Ring(val gameField: GameField, val index: Int) : Iterable<Block> {


    override fun iterator() = blocks.iterator()

    private var blocks: Array<Block> = Array(gameField.getNumberOfSegments(), { segment -> Block(this, index, segment) })

    operator fun get(segment: Int) = blocks[segment]

    fun reset() = blocks.forEach(Block::reset)

    fun getTakenCount() = blocks.filter { it.isTaken() }.count()

    fun size() = gameField.getNumberOfSegments()

    fun isFull() = getTakenCount() == gameField.getNumberOfSegments()


    override fun toString(): String {
        return "Ring:$index {${blocks.map { it.row.toString() + (if (it.isEmpty()) "[_]" else "[X]") }.joinToString(",")}}"
    }

}