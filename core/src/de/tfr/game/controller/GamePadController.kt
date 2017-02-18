package de.tfr.game.controller

import com.badlogic.gdx.math.Vector2
import de.tfr.game.Controller
import de.tfr.game.lib.Logger
import de.tfr.game.lib.controls.GamePad
import de.tfr.game.lib.controls.XBox360Pad
import de.tfr.game.util.angle

/**
 * @author Tobse4Git@gmail.com
 */
class GamePadController(segments: Int, val controlListener: Controller.ControlListener) : GamePad() {
    val degreesPerSegment = 360 / segments

    companion object {
        val log = Logger.new(GamePadController::class)
    }

    override fun buttonDown(controller: com.badlogic.gdx.controllers.Controller, buttonCode: Int): Boolean {
        when (buttonCode) {
            XBox360Pad.Companion.BUTTON_A -> controlListener.controlEvent(Controller.Control.Green)
            XBox360Pad.Companion.BUTTON_B -> controlListener.controlEvent(Controller.Control.Red)
            XBox360Pad.Companion.BUTTON_X -> controlListener.controlEvent(Controller.Control.Blue)
            XBox360Pad.Companion.BUTTON_Y -> controlListener.controlEvent(Controller.Control.Yellow)

            XBox360Pad.Companion.BUTTON_LB -> controlListener.controlEvent(Controller.Control.Up)
            XBox360Pad.Companion.BUTTON_RB -> controlListener.controlEvent(Controller.Control.Down)
            XBox360Pad.Companion.BUTTON_BACK -> controlListener.controlEvent(Controller.Control.Esc)
        }
        return true
    }


    override fun leftStickMoved(valueX: Float, valueY: Float) {
        //Right -> 90°
        val angle = Vector2(valueX, valueY).angle(90F + (degreesPerSegment / 2))
        val segment = (angle / degreesPerSegment).toInt()
        controlListener.controlEventSetSegment(Controller.SegmentActionType.Selected, segment)
    }
}
