package de.tfr.game.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled
import de.tfr.game.Enemy
import de.tfr.game.EnemyAI
import de.tfr.game.lib.actor.Point
import de.tfr.game.lib.actor.Point2D
import de.tfr.game.model.Block
import de.tfr.game.model.GameField
import de.tfr.game.model.Ring
import de.tfr.game.model.Stone
import de.tfr.game.ui.GameColor


/**
 * @author Tobse4Git@gmail.com
 */
class GameFieldRenderer(point: Point, val field: GameField, val camera: Camera) : Point by point {

    private val gap = 14
    private val blockWith = 36f
    private val radius = 18f
    private val radiusStoned = radius + 2
    private val radiusPlayer = radiusStoned + 8
    private val renderer = ShapeRenderer()

    class Colors {
        companion object {
            val emptyField = Color.GRAY
            val stoneRed = GameColor.Red
            val stoneBlue = GameColor.Blue
            val stoneYellow = GameColor.Yellow
            val stoneGreen = GameColor.Green

            val enemyRed = Color.RED//GameColor.RedLight
            val enemyBlue = Color.BLUE//}GameColor.BlueLight
            val enemyYellow = Color.YELLOW //GameColor.YellowLight
            val enemyGreen = Color.GREEN//GameColor.GreenLight
        }
    }

    fun render(enemyAI: EnemyAI) {
        enemyAI.enemies.forEach { renderEnemy(it) }
    }

    private fun renderEnemy(enemy: Enemy) {
        var i = enemy.stones.size
        var enemyGrow = 6f
        enemy.stones.forEach {
            val renderPos = getPos(it.block)
            renderer.color = getRenderColor(it)
            renderer.circle(renderPos.x, renderPos.y, radiusStoned + (i * enemyGrow))
            i--
        }
    }

    fun start() {
        renderer.projectionMatrix = camera.combined
        renderer.begin(Filled)
    }

    fun render() {
        //  renderBackground()

        renderer.color = Colors.emptyField
        renderer.circle(x, y, radius)
        field.forEach(this::renderRing)
        renderPlayer(field.player)
        // renderColors()
        // renderBorder()
    }

    /**
     * for debugging purpose
     */
    private fun renderColors() {
        val x = this.x - 600
        val y = this.y - 600

        var i = 0
        for (stoneColor in Stone.Color.values()) {
            renderer.color = getEnemyRenderColor(stoneColor)
            renderer.circle(x + (i++ * 24), y, 12f)
        }

        for (stoneColor in Stone.Color.values()) {
            renderer.color = getRenderColor(stoneColor)
            renderer.circle(x + (i++ * 24), y + 30, 12f)
        }
    }

    private fun renderPlayer(player: Stone) {
        val playerPos = getPos(player.block)
        renderer.color = Color.WHITE
        renderer.circle(playerPos.x, playerPos.y, radiusPlayer + 4)
        renderer.color = getRenderColor(player.color)
        renderer.circle(playerPos.x, playerPos.y, radiusPlayer)

    }

    private fun renderBackground() {
        renderer.color = Color.BLACK
        val radius = getFieldSize()
        renderer.rect(x - radius, y - radius, radius * 2, radius * 2)
    }

    fun getFieldSize(): Float = (blockWith / 2) + field.getNumberOfSegments() * (gap + blockWith)

    fun end() {
        renderer.end()
    }

    private fun renderRing(ring: Ring) {
        ring.forEach { renderBlock(it, it.stone) }
    }

    fun renderStone(stone: Stone) {
        renderBlock(stone.block, stone)
    }

    fun getPos(block: Block): Point {
        val distance = gap + blockWith + (block.row * (gap + blockWith))
        val singleRotation = (360 / field.getNumberOfSegments()).toFloat()
        val degree = block.segment * singleRotation
        val x = this.x + (distance * Math.sin(Math.toRadians(degree.toDouble()))).toFloat()
        val y = this.y + (distance * Math.cos(Math.toRadians(degree.toDouble()))).toFloat()
        return Point2D(x, y)
    }

    fun getRenderColor(stone: Stone): Color {
        return getEnemyRenderColor(stone.color)
/*
        if(stone.state == Stone.State.Incoming){
            return getEnemyRenderColor(stone.color)
        }else{
            return getRenderColor(stone.color)
        }*/
    }

    fun getEnemyRenderColor(color: Stone.Color): Color {
        when (color) {
            Stone.Color.Blue -> return Colors.enemyBlue
            Stone.Color.Green -> return Colors.enemyGreen
            Stone.Color.Yellow -> return Colors.enemyYellow
            Stone.Color.Red -> return Colors.enemyRed
        }
    }

    fun getRenderColor(color: Stone.Color): Color {
        when (color) {
            Stone.Color.Blue -> return Colors.stoneBlue
            Stone.Color.Green -> return Colors.stoneGreen
            Stone.Color.Yellow -> return Colors.stoneYellow
            Stone.Color.Red -> return Colors.stoneRed
        }
    }

    private fun renderBlock(block: Block, stone: Stone?) {
        val renderPos = getPos(block)

        if (stone != null) {
            if (stone.state == Stone.State.Wall) {
                renderer.color = Color.LIGHT_GRAY
                renderer.circle(renderPos.x, renderPos.y, radiusStoned + 4)
            }
            renderer.color = getRenderColor(stone)
            renderer.circle(renderPos.x, renderPos.y, radiusStoned)
        } else {
            renderer.color = Colors.emptyField
            renderer.circle(renderPos.x, renderPos.y, radius)
        }
    }

}