package de.tfr.game.lib.controls

import com.badlogic.gdx.controllers.PovDirection


/**
 * This code was taken from http://www.java-gaming.org/index.php?topic=29223.0
 * With thanks that is!
 *
 * It seems there are different versions of gamepads with different ID
Strings.
 * Therefore its IMO a better bet to check for:
 * if (controller.getName().toLowerCase().contains("xbox") &&
controller.getName().contains("360"))
 *
 * Controller (Gamepad for Xbox 360)
Controller (XBOX 360 For Windows)
Controller (Xbox 360 Wireless Receiver for Windows)
Controller (Xbox wireless receiver for windows)
XBOX 360 For Windows (Controller)
Xbox 360 Wireless Receiver
Xbox Receiver for Windows (Wireless Controller)
Xbox wireless receiver for windows (Controller)
 */
class XBox360Pad {
    companion object {
        val ID = "XBOX 360 For Windows (Controller)"
        val BUTTON_X = 2
        val BUTTON_Y = 3
        val BUTTON_A = 0
        val BUTTON_B = 1
        val BUTTON_BACK = 6
        val BUTTON_START = 7
        val BUTTON_DPAD_UP = PovDirection.north
        val BUTTON_DPAD_DOWN = PovDirection.south
        val BUTTON_DPAD_RIGHT = PovDirection.east
        val BUTTON_DPAD_LEFT = PovDirection.west
        val BUTTON_LB = 4
        val BUTTON_L3 = 8
        val BUTTON_RB = 5
        val BUTTON_R3 = 9
        val AXIS_LEFT_X = 1 //-1 is left | +1 is right
        val AXIS_LEFT_Y = 0 //-1 is up | +1 is down
        val AXIS_LEFT_TRIGGER = 4 //value 0 to 1f
        val AXIS_RIGHT_X = 3 //-1 is left | +1 is right
        val AXIS_RIGHT_Y = 2 //-1 is up | +1 is down
        val AXIS_RIGHT_TRIGGER = 4 //value 0 to -1f
    }


}