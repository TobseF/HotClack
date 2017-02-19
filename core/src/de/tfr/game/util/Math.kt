package de.tfr.game.util

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

/**
 * @author Tobse4Git@gmail.com
 */
fun Vector2.angle(correction: Float): Float {
    var angle = this.angle() + correction
    if (angle < 0) {
        angle = 360 + angle
    }
    if (angle > 360) {
        angle = angle - 360

    }
    return angle
}

/**
 * @return random alternation
 */
fun Float.randomChange(percent: Float): Float = MathUtils.random() * (percent * this)

fun Float.randomMutateUp(percent: Float): Float = this + this.randomChange(percent)
fun Float.randomMutateDown(percent: Float): Float = this - this.randomChange(percent)

fun Float.randomMutate(percent: Float): Float {
    var mutation = this.randomChange(percent)
    if (MathUtils.randomBoolean()) {
        mutation = -mutation
    }
    return this + mutation
}