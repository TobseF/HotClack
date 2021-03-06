package de.tfr.game.lib.controls

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerListener
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.controllers.PovDirection
import com.badlogic.gdx.math.Vector3
import de.tfr.game.lib.Logger

/**
 * @author Tobse4Git@gmail.com
 */
open class GamePad : ControllerListener {
    var noiseReduction = 0.4

    init {
        Controllers.addListener(this)
    }

    companion object {
        val log = Logger.new(GamePad::class)
    }

    override fun connected(controller: Controller) {
        log.debug(controller.toString())
    }

    override fun buttonUp(controller: Controller, buttonCode: Int): Boolean {
        return true
    }

    override fun ySliderMoved(controller: Controller, sliderCode: Int, value: Boolean): Boolean {
        return true
    }

    override fun accelerometerMoved(controller: Controller, accelerometerCode: Int, value: Vector3?): Boolean {
        return true
    }

    override fun axisMoved(controller: Controller, axisCode: Int, value: Float): Boolean {

        //println("axisMoved = axisCode: " + axisCode + ": " + value)
        if (value > noiseReduction || value < -noiseReduction) {
            if // Left Stick
                    (axisCode == XBox360Pad.AXIS_LEFT_X || axisCode == XBox360Pad.AXIS_LEFT_Y) {
                leftStickMoved(controller.getAxis(XBox360Pad.AXIS_LEFT_X), controller.getAxis(XBox360Pad.AXIS_LEFT_Y))
            } else if // Right stick
                           (axisCode == XBox360Pad.AXIS_RIGHT_X || axisCode == XBox360Pad.AXIS_RIGHT_Y) {
                rightStickMoved(controller.getAxis(XBox360Pad.AXIS_RIGHT_X), controller.getAxis(XBox360Pad.AXIS_RIGHT_Y))
            }
        } else {
            //   println("noise: axisMoved = axisCode: " + axisCode + ": " + value)
        }
        return false
    }

    open fun leftStickMoved(valueX: Float, valueY: Float) {

    }

    open fun rightStickMoved(valueX: Float, valueY: Float) {

    }

    override fun disconnected(controller: Controller) {
    }

    override fun xSliderMoved(controller: Controller, sliderCode: Int, value: Boolean): Boolean {
        return true
    }

    override fun povMoved(controller: Controller, povCode: Int, value: PovDirection?): Boolean {
        return true
    }

    override fun buttonDown(controller: Controller, buttonCode: Int): Boolean {
        return true
    }
}