package de.tfr.game.util

/**
 * @author Tobse4Git@gmail.com
 */
open class Timer(var actionTime: Float, val timerAction: (deltaTime: Float) -> Unit = { _ -> }, runOnStart: Boolean = true, private var infinite: Boolean = true) : Time {

    override var time = 0F
    private var pause = false

    init {
        pause = !runOnStart
    }

    constructor(actionTime: Float, simpleTimerAction: () -> Unit, runOnStart: Boolean = true, infinite: Boolean = true) : this(actionTime, { _ -> simpleTimerAction.invoke() }, runOnStart, infinite)

    fun update(deltaTime: Float) {
        time += deltaTime
        if (!pause && time >= actionTime) {
            if (!infinite) {
                pause = true
            }
            time = 0F
            timerAction.invoke(deltaTime)
        }
    }

    fun togglePause() {
        pause = !pause
    }

    fun start() {
        pause = false
    }

    fun reStart() {
        reset()
        start()
    }

    fun stop() {
        pause = true
    }

    fun reset() {
        time = 0F
    }

    fun isRunning() = !pause
    fun isPaused() = pause

}
