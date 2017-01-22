package de.tfr.game

import de.tfr.game.util.StopWatch
import de.tfr.game.util.TimerFormatter

/**
 * @author Tobse4Git@gmail.com
 */
class TimeDisplay {

    var watch = StopWatch()
    var formatter = TimerFormatter()

    fun getText() = formatter.getFormattedTimeAsString(watch.getTime())

    fun reset() {
        watch.reset()
    }
}
