package de.tfr.game

import com.badlogic.gdx.math.MathUtils
import de.tfr.game.lib.random
import de.tfr.game.model.GameField

/**
 * @author Tobse4Git@gmail.com
 */
class EnemyAI(val field: GameField) {

    val enemies = mutableListOf<Enemy>()
    val enemyRate = RandomStep(5)

    fun doStep() {
        enemies.forEach { it.doStep() }
        if (enemyRate.doStep()) {
            spawnEnemy()
        }

    }

    private fun spawnEnemy() {
        val nextSegment = nextSegment()
        if (nextSegment != null) {
            var new = Enemy(field, nextSegment)
            enemies += new
        }
    }

    class RandomStep(val chance: Int) {
        val nextStep = MathUtils.random(chance)

        fun doStep() = MathUtils.random(chance) == nextStep
    }

    fun listTakenSegments() = enemies.map { it.segment }

    fun listFreeSegments() = (0..field.segments() - 1).filter { !listTakenSegments().contains(it) }.toList()

    fun nextSegment() = listFreeSegments().random()

}