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
        return true
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