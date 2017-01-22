package de.tfr.game.util

/**
 * @author Tobse4Git@gmail.com
 */
open class Timer(var actionTime: Float, val timerAction: (deltaTime: Float) -> Unit) : Time {

    constructor(actionTime: Float, simpleTimerAction: () -> Unit) : this(actionTime, { _ -> simpleTimerAction.invoke() })

    override var time = 0F
    private var pause = false
    private var infinite = true

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

    fun reset() {
        time = 0F
    }

}
