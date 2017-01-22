package de.tfr.game

import de.tfr.game.model.GameField

/**
 * @author Tobse4Git@gmail.com
 */
class EnemyAI(val field: GameField) {

    val enemies = mutableListOf<Enemy>()
    val enemiesToRemove = mutableListOf<Enemy>()
    val enemiesMap = HashMap<Int, Enemy>()
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
            var new = Enemy(field, this, nextSegment)
            enemiesMap.put(nextSegment, new)
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
    fun shoot(segment: Int) {
        val enemy = enemiesMap[segment]
        if (enemy != null) {
            shoot(enemy)
        }
    }

    private fun shoot(enemy: Enemy) {
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