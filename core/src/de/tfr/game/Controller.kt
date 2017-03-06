package de.tfr.game

import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import de.tfr.game.Controller.Control.*
import de.tfr.game.Controller.ControlEvent.ControlEvenType.Clicked
import de.tfr.game.Controller.ControlEvent.ControlEvenType.Selected
import de.tfr.game.lib.Logger
import de.tfr.game.lib.actor.Point
import de.tfr.game.lib.controls.GamePad
import de.tfr.game.model.Stone
import de.tfr.game.renderer.ColorChooserRenderer
import de.tfr.game.util.angle
import de.tfr.game.util.extensions.Vector2
import java.util.*


/**
 * @author Tobse4Git@gmail.com
 */
class Controller(point: Point, private val gameRadius: Float, val viewport: Viewport, segments: Int, val colorChooseRenderer: ColorChooserRenderer) : InputProcessor by InputAdapter(), Point by point {

    companion object {
        val Log = Logger.new(GamePad::class)
    }

    private val touchListeners: MutableCollection<ControlListener> = ArrayList()

    enum class Control {Up, Down, Left, Right, Blue, Red, Yellow, Green, Esc, Action, Pause }

    class ControlEvent(val type: ControlEvenType, val control: Control) {
        enum class ControlEvenType {Selected, Clicked }
    }

    val degreesPerSegment = 360 / segments
    val degreesPerHalfSegment = degreesPerSegment / 2
    val deviationMax = 10

    interface ControlListener {
        fun controlEvent(controlEvent: ControlEvent)
        fun controlEvent(control: Control) = controlEvent(ControlEvent(Clicked, control))
        fun controlEventSetSegment(type: SegmentActionType, segment: Int)
    }

    class TouchPoint(inputIndex: Int) : Vector2(input.getX(inputIndex).toFloat(), input.getY(inputIndex).toFloat())


    init {
        input.inputProcessor = this
        input.isCatchBackKey = true
    }

    private fun getTouchPointers() = (0..6).filter(input::isTouched).map { viewport.unproject(TouchPoint(it)) }.filter { !it.isZero }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val worldCords = viewport.unproject(Vector2(screenX, screenY))
        chooseColor(worldCords)
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val screenCords = Vector2(screenX, screenY)
        val worldCords = viewport.unproject(screenCords.cpy())
        checkSegmentSelection(screenCords, worldCords)
        chooseColor(worldCords)
        return true
    }

    private fun checkSegmentSelection(screenCords: Vector2, worldCords: Vector2?) {
        if (Vector2(x, y).sub(worldCords).len() <= gameRadius) {
            val angleSegment = posToAngle(screenCords, 90F - degreesPerHalfSegment)
            val angle = posToAngle(screenCords, (0f))
            var deviation = angle % degreesPerSegment
            if (deviation >= degreesPerHalfSegment) {
                deviation = degreesPerSegment - deviation
            }
            if (deviation < deviationMax) {
                val segment = (angleSegment / degreesPerSegment).toInt()
                notifyListener(SegmentActionType.Clicked, segment)
            }
        }
    }

    private fun chooseColor(worldCords: Vector2?) {
        colorChooseRenderer.touchAreas.filter { it.rect.contains(worldCords) }.forEach {
            notifyListener(ControlEvent(Selected, it.getControl()))
        }
    }

    override fun scrolled(amount: Int): Boolean {
        if (amount > 0) {
            notifyListenerAsLickEvent(Down)
        } else {
            notifyListenerAsLickEvent(Up)
        }
        return true
    }


    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val screenCords = Vector2(screenX, screenY)
        val angleSegment = posToAngle(screenCords, 90F - degreesPerHalfSegment)
        val segment = (angleSegment / degreesPerSegment).toInt()
        notifyListener(SegmentActionType.Selected, segment)
        return false
    }

    private fun posToAngle(screenCords: Vector2, start: Float): Float {
        val worldCords = viewport.unproject(screenCords.cpy())
        val angle = 360 - Math.abs(Vector2(x, y).sub(worldCords).
                angle(start))
        return angle
    }

    override fun keyDown(keycode: Int): Boolean {
        fun toControl(keycode: Int) =
                when (keycode) {
                    Keys.RIGHT -> Right
                    Keys.UP -> Up
                    Keys.DOWN -> Down
                    Keys.LEFT -> Left
                    Keys.NUM_1, Keys.NUMPAD_1 -> Green
                    Keys.NUM_2, Keys.NUMPAD_2 -> Red
                    Keys.NUM_3, Keys.NUMPAD_3 -> Blue
                    Keys.NUM_4, Keys.NUMPAD_4 -> Yellow
                    Keys.SPACE -> Action
                    Keys.P -> Pause
                    Keys.ESCAPE, Keys.BACK -> Esc
                    else -> null
                }
        toControl(keycode)?.let(this::notifyListenerAsLickEvent)
        return true
    }

    fun addTouchListener(touchListener: ControlListener) = touchListeners.add(touchListener)

    private fun notifyListenerAsLickEvent(control: Control) = notifyListener(ControlEvent(Clicked, control))

    private fun notifyListener(control: ControlEvent) = touchListeners.forEach { it.controlEvent(control) }

    private fun notifyListener(type: SegmentActionType, segment: Int) = touchListeners.forEach { it.controlEventSetSegment(type, segment) }

    enum class SegmentActionType {Selected, Clicked }

}

fun Controller.Control.toColor(): Stone.Color? = when (this) {
    Controller.Control.Blue -> Stone.Color.Blue
    Controller.Control.Green -> Stone.Color.Green
    Controller.Control.Yellow -> Stone.Color.Yellow
    Controller.Control.Red -> Stone.Color.Red
    else -> null
}