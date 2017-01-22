package de.tfr.game

import de.tfr.game.model.GameField
import de.tfr.game.model.Stone
import de.tfr.game.util.Timer

/**
 * @author Tobse4Git@gmail.com
 */
class Enemy(val field: GameField, val enemyAI: EnemyAI, val segment: Int) {

    val color: Stone.Color = Stone.Color.random()

    private var death: Timer? = null

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

    fun shrink() {
        if (stones.isNotEmpty()) {
            stones.remove(stones.last())
        } else {
            die()
        }
    }

    private fun die() {
        enemyAI.remove(this)
    }

    fun shoot() {
        if (death == null) {
            death = Timer(0.1f, this::shrink)
        }
    }

    fun update(deltaTime: Float) {
        death?.update(deltaTime)
    }

}