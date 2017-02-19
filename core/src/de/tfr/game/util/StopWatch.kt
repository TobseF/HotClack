package de.tfr.game.util

import java.lang.System.currentTimeMillis

/**
 * @author Tobse4Git@gmail.com
 */
class StopWatch(var start: ms = currentTimeMillis()) {

    var pause: Boolean = false
    var pauseTime: ms = 0

    fun getTime(): ms = if (pause) (pauseTime - start) else (currentTimeMillis() - start)

    fun reset() {
        start = currentTimeMillis()
        pause = false
    }

    fun pause() = true

    fun togglePause() {
        pause = !pause
        if (pause) {
            pauseTime = currentTimeMillis()
        } else {
            start += (currentTimeMillis() - pauseTime)
        }
    }
}
