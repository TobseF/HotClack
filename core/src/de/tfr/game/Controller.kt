package de.tfr.game

import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import de.tfr.game.Controller.Control.*
import de.tfr.game.lib.Logger
import de.tfr.game.lib.actor.Point
import de.tfr.game.lib.controls.GamePad
import de.tfr.game.util.angle
import de.tfr.game.util.extensions.Vector2
import de.tfr.game.util.extensions.unproject
import java.util.*


/**
 * @author Tobse4Git@gmail.com
 */
class Controller(point: Point, gameRadius: Float, val viewport: Viewport, segments: Int) : InputProcessor by InputAdapter(), Point by point {

    companion object {
        val log = Logger.new(GamePad::class)
    }

    val left: TouchArea
    val right: TouchArea
    val top: TouchArea
    val bottom: TouchArea

    private val distance = 90f
    private val radius = 62f
    private val vibrateTime = 26

    private val touchListeners: MutableCollection<ControlListener> = ArrayList()

    enum class Control {Up, Down, Left, Right, Blue, Red, Yellow, Green, Esc, Action, Pause }
    private class Button(centerX: Float, centerY: Float, radius: Float) : Rectangle(centerX - radius, centerY - radius, radius * 2, radius * 2)
    class TouchArea(val control: Control, val rect: Rectangle)

    val degreesPerSegment = 360 / segments

    interface ControlListener {
        fun controlEvent(control: Control)
        fun controlEventSetSegment(segment: Int)
    }

    class TouchPoint(inputIndex: Int) : Vector2(input.getX(inputIndex).toFloat(), input.getY(inputIndex).toFloat())

    fun isPressed(control: Control): Boolean {
        val touchPointers = getTouchPointers()
        fun touches(touchArea: TouchArea) = touchPointers.any(touchArea.rect::contains)
        when (control) {
            Left -> return input.isKeyPressed(Keys.LEFT) || touches(left)
            Right -> return input.isKeyPressed(Keys.RIGHT) || touches(right)
            Up -> return input.isKeyPressed(Keys.UP) || touches(top)
            Down -> return input.isKeyPressed(Keys.DOWN) || touches(bottom)
            Blue -> return input.isKeyPressed(Keys.NUM_1) || input.isKeyPressed(Keys.NUMPAD_1) || touches(bottom)
            Red -> return input.isKeyPressed(Keys.NUM_2) || input.isKeyPressed(Keys.NUMPAD_2) || touches(bottom)
            Yellow -> return input.isKeyPressed(Keys.NUM_3) || input.isKeyPressed(Keys.NUMPAD_3) || touches(bottom)
            Green -> return input.isKeyPressed(Keys.NUM_4) || input.isKeyPressed(Keys.NUMPAD_4) || touches(bottom)
        }
        return false
    }

    private fun getTouchPointers() = (0..6).filter(input::isTouched).map { viewport.unproject(TouchPoint(it)) }.filter { !it.isZero }

    init {
        left = TouchArea(Blue, Button(x - gameRadius - distance, y, radius))
        right = TouchArea(Red, Button(x + gameRadius + distance, y, radius))
        top = TouchArea(Yellow, Button(x, y + gameRadius + distance, radius))
        bottom = TouchArea(Green, Button(x, y - gameRadius - distance, radius))
        input.inputProcessor = this
        input.isCatchBackKey = true
    }

    val touchAreas: List<TouchArea> by lazy {
        arrayListOf(left, right, top, bottom)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val worldCords = viewport.unproject(screenX, screenY)
        touchAreas.filter { it.rect.contains(worldCords) }.forEach {
            doHapticFeedback()
            notifyListener(it.control)
        }
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val screenCords = Vector2(screenX, screenY)
        val worldCords = viewport.unproject(screenCords)
        val angle = 360 - Math.abs(Vector2(x, y).sub(worldCords).
                angle(90F - (degreesPerSegment / 2)))
        val segment = (angle / degreesPerSegment).toInt()
        log.debug("angle: ${angle} segment: ${segment}")
        notifyListener(segment)
        return false
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
        toControl(keycode)?.let(this::notifyListener)
        doHapticFeedback()
        return true
    }

    private fun doHapticFeedback() = input.vibrate(vibrateTime)

    fun addTouchListener(touchListener: ControlListener) = touchListeners.add(touchListener)

    private fun notifyListener(control: Control) = touchListeners.forEach { it.controlEvent(control) }

    private fun notifyListener(segment: Int) = touchListeners.forEach { it.controlEventSetSegment(segment) }


}