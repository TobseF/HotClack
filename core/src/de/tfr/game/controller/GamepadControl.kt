package de.tfr.game.controller

import de.tfr.game.Controller
import de.tfr.game.lib.controls.GamePad
import de.tfr.game.lib.controls.XBox360Pad

/**
 * @author Tobse4Git@gmail.com
 */
class GamepadControl(val controlListener: Controller.ControlListener) : GamePad() {

    override fun buttonDown(controller: com.badlogic.gdx.controllers.Controller, buttonCode: Int): Boolean {
        when (buttonCode) {
            XBox360Pad.Companion.BUTTON_A -> controlListener.controlEvent(Controller.Control.Green)
            XBox360Pad.Companion.BUTTON_B -> controlListener.controlEvent(Controller.Control.Red)
            XBox360Pad.Companion.BUTTON_X -> controlListener.controlEvent(Controller.Control.Blue)
            XBox360Pad.Companion.BUTTON_Y -> controlListener.controlEvent(Controller.Control.Yellow)
        }
        return true
    }
}
