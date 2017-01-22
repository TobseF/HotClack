package de.tfr.game

import com.badlogic.gdx.math.MathUtils.randomBoolean
import de.tfr.game.lib.random
import de.tfr.game.model.GameField
import de.tfr.game.util.Timer

/**
 * @author Tobse4Git@gmail.com
 */
class EnemyAI(val field: GameField) {

    val enemies = mutableListOf<Enemy>()
    private val enemiesToRemove = mutableListOf<Enemy>()
    private val enemiesMap = HashMap<Int, Enemy>()
    private val firstPause = 0.7f
    private val incomingSpeedMax = 0.1f
    private val incomingLoop = Timer(firstPause, this::doStep)
    private val enemySpeed = 1.5f
    private val spawnRate = 0.1f

    fun doStep(deltaTime: Float) {
        incomingLoop.actionTime = incomingSpeedMax
        enemies.forEach { it.doStep(deltaTime) }
        if (randomBoolean(spawnRate)) {
            spawnEnemy()
        }
    }

    private fun spawnEnemy() {
        val nextSegment = nextSegment()
        if (nextSegment != null) {

            var new = Enemy(field, this, nextSegment, enemySpeed)
            enemiesMap.put(nextSegment, new)
            enemies += new
        }
    }

    fun listTakenSegments() = enemies.map { it.segment }

    fun listFreeSegments() = (0..field.segments() - 1).filter { !listTakenSegments().contains(it) }.toList()

    fun nextSegment() = listFreeSegments().random()


    fun getEnemy(segment: Int): Enemy? = enemiesMap[segment]

    fun shoot(enemy: Enemy) {
        enemy.shoot()
    }

    fun remove(enemy: Enemy) {
        enemiesToRemove += enemy
    }

    fun update(deltaTime: Float) {
        enemies.forEach { it.update(deltaTime) }
        val enemiesToRemoveIterator = enemiesToRemove.iterator();
        while (enemiesToRemoveIterator.hasNext()) {
            val kill = enemiesToRemoveIterator.next()
            enemies.remove(kill)
            enemiesMap.remove(kill.segment)
            enemiesToRemoveIterator.remove()
        }

    }

}