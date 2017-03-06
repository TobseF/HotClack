package de.tfr.game

import com.badlogic.gdx.Gdx.input
import de.tfr.game.model.Stone
import de.tfr.game.util.extensions.next
import de.tfr.game.util.extensions.prev

/**
 * @author Tobse4Git@gmail.com
 */
class ColorChooser {
    private val vibrateTime = 24

    var color = Stone.Color.Green
        set(value) {
            if (value != color) {
                field = value
                doHapticFeedback()
            }
        }

    private fun doHapticFeedback() = input.vibrate(vibrateTime)


    var blocked = false

    fun next(): Stone.Color {
        color = color.next()
        return color
    }

    fun prev(): Stone.Color {
        color = color.prev()
        return color
    }

}