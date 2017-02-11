package de.tfr.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.MathUtils.randomBoolean
import de.tfr.game.model.GameField
import de.tfr.game.model.Stone
import de.tfr.game.util.Timer
import de.tfr.game.util.randomMutate
import de.tfr.game.util.randomMutateUp

/**
 * @author Tobse4Git@gmail.com
 */
class Enemy(val field: GameField, val enemyAI: EnemyAI, val segment: Int, speed: Float) {

    val speed = initRandomizedSpeed(speed)

    class Speed(var current: Float, var min: Float, var max: Float, val mutationRate: Float, timerAction: (deltaTime: Float) -> Unit) : Timer(current, timerAction) {
        fun mutateMe() = MathUtils.randomBoolean(mutationRate)
    }

    val color: Stone.Color = Stone.Color.random()

    private var death: Timer? = null

    fun initRandomizedSpeed(speed: Float): Speed {
        val current = speed.randomMutate(0.20f)
        val max = speed.randomMutate(0.15f)
        val min = speed.randomMutateUp(0.25f)
        val mutationRate = 0.1f.randomMutate(80f)
        return Speed(current, min, max, mutationRate, this::growEnemy)
    }

    fun mutateSpeed() {
        if (speed.mutateMe()) {
            speed.current = MathUtils.random(if (randomBoolean()) speed.min else speed.max)
        }
        speed.actionTime = speed.current
    }


    val stones = mutableListOf<Stone>()

    fun doStep(deltaTime: Float): Unit {
        mutateSpeed()
        speed.update(deltaTime)
    }

    fun growEnemy(deltaTime: Float): Unit {
        if (canGrow()) {
            if (!wasKilled()) {
                val new = Stone(field[getCurrentRingIndex() - 1][segment], color)
                new.state = Stone.State.Incoming
                stones += new
            }
        } else {
            enemyAI.enemyReachedBase()
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
    }

}