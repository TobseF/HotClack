package de.tfr.game.controller

import com.badlogic.gdx.math.Vector2
import de.tfr.game.Controller
import de.tfr.game.lib.controls.GamePad
import de.tfr.game.lib.controls.XBox360Pad

/**
 * @author Tobse4Git@gmail.com
 */
class GamePadController(val segments: Int, val controlListener: Controller.ControlListener) : GamePad() {
    val degreesPerSegment = 360 / segments

    override fun buttonDown(controller: com.badlogic.gdx.controllers.Controller, buttonCode: Int): Boolean {
        when (buttonCode) {
            XBox360Pad.Companion.BUTTON_A -> controlListener.controlEvent(Controller.Control.Green)
            XBox360Pad.Companion.BUTTON_B -> controlListener.controlEvent(Controller.Control.Red)
            XBox360Pad.Companion.BUTTON_X -> controlListener.controlEvent(Controller.Control.Blue)
            XBox360Pad.Companion.BUTTON_Y -> controlListener.controlEvent(Controller.Control.Yellow)

            XBox360Pad.Companion.BUTTON_LB -> controlListener.controlEvent(Controller.Control.Up)
            XBox360Pad.Companion.BUTTON_RB -> controlListener.controlEvent(Controller.Control.Down)
        }
        return true
    }

    override fun leftStickMoved(x: Float, y: Float) {
        //Right -> 90°
        var angle = Vector2(x, y).angle() + 90
        if (angle < 0) {
            angle = 360 + angle
        }
        if (angle > 360) {
            angle = angle - 360
        }

        val segment = angle / degreesPerSegment

        // println(angle.toString() + ":" + segment + " = " + segment.toInt())
        controlListener.controlEventSetSegment(segment.toInt())

        /* if (angle > (360 - (degreesPerSegment / 2))) {
             return controlListener.controlEventSetSegment(0)
         } else {
             controlListener.controlEventSetSegment((angle / segments).toInt())
         }*/
    }
}
