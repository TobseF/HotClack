package de.tfr.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.MathUtils.randomBoolean
import de.tfr.game.model.GameField
import de.tfr.game.model.Stone
import de.tfr.game.util.Timer

/**
 * @author Tobse4Git@gmail.com
 */
class Enemy(val field: GameField, val enemyAI: EnemyAI, val segment: Int, speed: Float) {

    val speed = initRandomezedSpeed(speed)

    class Speed(var current: Float, var min: Float, var max: Float, val mutationRate: Float, timerAction: (deltaTime: Float) -> Unit) : Timer(current, timerAction) {
        fun mutateMe() = MathUtils.randomBoolean(mutationRate)
    }

    val color: Stone.Color = Stone.Color.random()

    private var death: Timer? = null


    fun initRandomezedSpeed(speed: Float): Speed {
        val current = speed.randomMutate(0.20f)
        val max = speed.randomMutate(0.20f)
        val min = speed.randomMutateUp(0.40f)
        val mutationRate = 0.1f.randomMutate(80f)
        return Speed(current, min, max, mutationRate, this::growEnemy)
    }

    fun mutateSpeed() {
        if (speed.mutateMe()) {
            if (randomBoolean()) {
                speed.current = MathUtils.random(speed.min)
            } else {
                speed.current = MathUtils.random(speed.max)
            }
        }
        speed.actionTime = speed.current
    }

    /**
     * @return random alternation
     */
    fun Float.randomChange(percent: Float): Float = MathUtils.random() * (percent * this)

    fun Float.randomMutateUp(percent: Float): Float = this + this.randomChange(percent)

    fun Float.randomMutate(percent: Float): Float {
        var mutation = this.randomChange(percent)
        if (randomBoolean()) {
            mutation = -mutation
        }
        return this + mutation
    }

    val stones = mutableListOf<Stone>()

    fun doStep(deltaTime: Float): Unit {
        mutateSpeed()
        speed.update(deltaTime)
    }

    fun growEnemy(deltaTime: Float): Unit {
        if (canGrow()) {
            if (!wasKilled()) {
                val new = Stone(field[field.size - 1 - index()][segment], color)
                new.state = Stone.State.Incoming
                stones += new
            }
        } else {
            enemyAI.enemyReachedBase()
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

    fun wasKilled() = (death != null)

    fun update(deltaTime: Float) {
        death?.update(deltaTime)
    }

}