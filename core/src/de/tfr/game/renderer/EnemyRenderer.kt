package de.tfr.game.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import de.tfr.game.Enemy
import de.tfr.game.EnemyAI
import de.tfr.game.model.GameField

/**
 * @author Tobse4Git@gmail.com
 */
class EnemyRenderer(val gameField: GameField, val enemyAI: EnemyAI, camera: Camera) {

    val renderer: ShapeRenderer

    init {
        renderer = ShapeRenderer()
        renderer.projectionMatrix = camera.combined
    }

    fun render() {
        enemyAI.enemies.forEach { renderEnemy(it) }
    }

    private fun renderEnemy(enemy: Enemy) {

    }
}