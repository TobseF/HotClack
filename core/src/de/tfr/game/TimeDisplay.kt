package de.tfr.game

import de.tfr.game.lib.actor.Box
import de.tfr.game.util.StopWatch
import de.tfr.game.util.TimerFormatter

/**
 * @author Tobse4Git@gmail.com
 */
class TimeDisplay {

    var watch = StopWatch()
    var formatter = TimerFormatter()

    fun getText() = formatter.getFormattedTimeAsString(watch.getTime())
}