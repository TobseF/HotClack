package de.tfr.game

import de.tfr.game.model.GameField
import de.tfr.game.model.Stone

/**
 * @author Tobse4Git@gmail.com
 */
class Enemy(val field: GameField, val segment: Int) {

    val color: Stone.Color = Stone.Color.random()

    val stones = mutableListOf<Stone>()

    fun doStep() {
        if (canGrow()) {
            val new = Stone(field[field.size - 1 - index()][segment], color)
            new.state = Stone.State.Incoming
            stones += new
        }

    }

    fun canGrow() = (field.size - index()) > 0

    fun index() = stones.size

}