package de.tfr.game

import de.tfr.game.model.Stone
import de.tfr.game.util.extensions.next
import de.tfr.game.util.extensions.prev

/**
 * @author Tobse4Git@gmail.com
 */
class ColorChooser {
    var color = Stone.Color.Green
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