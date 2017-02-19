package de.tfr.game.model

import de.tfr.game.lib.random

/**
 * @author Tobse4Git@gmail.com
 */
open class Stone(private val initBlock: Block, var color: Color) {

    enum class Color {Green, Blue, Red, Yellow;

        companion object {
            fun random() = values().random()
        }
    }

    enum class State {Wall, Incoming, Active, Set }

    var size = 0

    var state = State.Active

    var block: Block = initBlock
        set(value) {
            field = value
        }

    fun freeze() {
        state = State.Set
    }

    override fun toString(): String {
        return "Stone[$state]"
    }

    fun clone(): Stone {
        val copy = Stone(initBlock, this.color)
        copy.state = this.state
        return copy
    }
}