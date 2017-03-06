package de.tfr.game.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import de.tfr.game.ColorChooser
import de.tfr.game.Controller
import de.tfr.game.config.GameConfig
import de.tfr.game.lib.actor.Point
import de.tfr.game.lib.actor.Point2D
import de.tfr.game.model.Stone

/**
 * @author Tobse4Git@gmail.com
 *
 *
 */
class ColorChooserRenderer(config: GameConfig.ColorChooserRendererConf, val colorChooser: ColorChooser, val renderer: ShapeRenderer, val camera: Camera) : Point by config.position {

    class TouchArea(val color: Stone.Color, val rect: Rectangle) {

        fun getControl() = toControl(color)

        fun toControl(color: Stone.Color): Controller.Control = when (color) {
            Stone.Color.Blue -> Controller.Control.Blue
            Stone.Color.Green -> Controller.Control.Green
            Stone.Color.Yellow -> Controller.Control.Yellow
            Stone.Color.Red -> Controller.Control.Red
        }
    }

    class Circle(point: Point, var radius: Float) : Point by point

    val radius = config.radius
    val gap = config.gap
    val selectionWidth = config.selectionWidth
    val selectionGap = config.selectionGap
    val colorRadius = radius - selectionWidth - selectionGap

    val touchAreas: List<TouchArea> = Stone.Color.values().map(this::getTouchArea).toList()

    fun getCircle(color: Stone.Color) = Circle(Point2D(x + (color.ordinal * ((radius * 2) + gap)), y), radius)

    fun getTouchArea(color: Stone.Color): TouchArea {
        val circle = getCircle(color)
        val side = radius * 2
        return TouchArea(color, Rectangle(circle.x - circle.radius, circle.y - circle.radius, side, side))
    }

    fun render() {
        renderer.projectionMatrix = camera.combined
        renderer.begin(ShapeRenderer.ShapeType.Filled)

        for (color in Stone.Color.values()) {
            val circle = getCircle(color)
            if (colorChooser.color == color) {
                renderer.color = Color.WHITE
                renderCircle(circle)
                circle.radius = radius - selectionWidth
                renderer.color = Color.BLACK
                renderCircle(circle)
            }
            circle.radius = colorRadius
            if (colorChooser.blocked) {
                renderer.color = Color.DARK_GRAY
            } else {
                renderer.color = getEnemyRenderColor(color)
            }
            renderCircle(circle)
        }

        renderer.end()
    }


    fun renderCircle(circle: Circle) = renderer.circle(circle.x, circle.y, circle.radius, 120)

    fun renderTouchArea(area: TouchArea) {
        renderer.color = Color.BLUE
        val rect = area.rect
        renderer.rect(rect.x, rect.y, rect.width, rect.height)
    }


}