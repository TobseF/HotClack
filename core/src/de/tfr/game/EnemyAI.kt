package de.tfr.game

import de.tfr.game.lib.Log
import de.tfr.game.lib.Logger
import de.tfr.game.lib.random
import de.tfr.game.model.GameField
import de.tfr.game.util.Timer
import de.tfr.game.util.randomMutate

/**
 * @author Tobse4Git@gmail.com
 */
class EnemyAI(val field: GameField, val gameOverListener: () -> Unit) {

    val enemies = mutableListOf<Enemy>()

    companion object {
        val log: Log = Logger.new(EnemyAI::class)
    }

    private val enemiesToRemove = mutableListOf<Enemy>()
    private val enemiesMap = HashMap<Int, Enemy>()
    private val enemySpeedStart = 1.5f
    private val spawnSpeedStart = 2.2f

    private var spawnSpeed = spawnSpeedStart
    private val spawnLoop = Timer(0f, this::spawnEnemyLoop)
    private val enemySpeedUp = 0.005f
    private val spawnSpeedUp = 0.02f

    private var enemySpeed = enemySpeedStart
    private var paused = false
    private var killedEnemies = 0

    fun spawnEnemyLoop() {
        spawnEnemy()
        spawnLoop.actionTime = spawnSpeed.randomMutate(0.25f)
    }

    fun enemyReachedBase() {
        gameOverListener.invoke()
    }

    private fun spawnEnemy() {
        val nextSegment = nextSegment()
        if (nextSegment != null) {
            val enemy = Enemy(field, this, nextSegment, enemySpeed)
            enemiesMap.put(nextSegment, enemy)
            enemies += enemy
        }
    }

    fun listTakenSegments() = enemies.map { it.segment }

    fun listFreeSegments() = (0 until field.getNumberOfSegments()).filter { !listTakenSegments().contains(it) }.toList()

    fun nextSegment() = listFreeSegments().random()

    fun getEnemy(segment: Int): Enemy? = enemiesMap[segment]

    fun shoot(enemy: Enemy) {
        enemySpeed -= enemySpeedUp
        spawnSpeed -= spawnSpeedUp
        enemy.shoot()
        log.debug("spawnSpeed $spawnSpeed enemySpeed: $enemySpeed")
    }

    fun remove(enemy: Enemy) {
        enemiesToRemove += enemy
    }

    fun update(deltaTime: Float) {
        if (!paused) {
            spawnLoop.update(deltaTime)
            enemies.forEach { it.update(deltaTime) }
            val enemiesToRemoveIterator = enemiesToRemove.iterator()
            while (enemiesToRemoveIterator.hasNext()) {
                val kill = enemiesToRemoveIterator.next()
                enemies.remove(kill)
                enemiesMap.remove(kill.segment)
                enemiesToRemoveIterator.remove()
            }
        }
    }

    fun killAll() {
        enemies.forEach { shoot(it) }
    }

    fun pause() {
        paused = true
    }

    fun resume() {
        paused = false
    }

    fun reset() {
        killedEnemies = 0
        spawnSpeed = spawnSpeedStart
        enemySpeed = enemySpeedStart
        enemies.clear()
        enemiesToRemove.clear()
        enemiesMap.clear()
        resume()
    }
}