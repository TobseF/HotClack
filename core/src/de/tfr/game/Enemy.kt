package de.tfr.game

import de.tfr.game.model.GameField
import de.tfr.game.model.Stone
import de.tfr.game.util.Timer
import de.tfr.game.util.randomMutateUp

/**
 * @author Tobse4Git@gmail.com
 */
class Enemy(val field: GameField, val enemyAI: EnemyAI, val segment: Int, maxSpeed: Float) {

    val color: Stone.Color = Stone.Color.random()
    val stones = mutableListOf<Stone>()

    private val speed: Timer
    private var death: Timer? = null

    init {
        speed = Timer(maxSpeed.randomMutateUp(0.40f), this::enemyStep)
        grow()
    }

    fun enemyStep(): Unit {
        if (canGrow()) {
            grow()
        } else {
            enemyAI.enemyReachedBase()
        }
    }

    private fun grow() {
        if (!wasKilled()) {
            val new = Stone(field[getCurrentRingIndex() - 1][segment], color)
            new.state = Stone.State.Incoming
            stones += new
        }
    }

    private fun getCurrentRingIndex() = field.getNumberOfRings() - index()

    fun canGrow() = (field.getNumberOfRings() - index()) > 0

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

    fun wasKilled() = (death != null)

    fun update(deltaTime: Float) {
        death?.update(deltaTime)
        speed.update(deltaTime)
    }

}