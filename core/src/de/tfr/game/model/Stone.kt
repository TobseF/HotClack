package de.tfr.game.model

/**
 * @author Tobse4Git@gmail.com
 */
class Stone(private val initBlock: Block) {

    enum class Color {Green, Blue, Red, Yellow, Undefined }
    enum class State {Wall, Incoming, Active, Set }

    var color = Color.Undefined

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
        var copy = Stone(initBlock)
        copy.color = this.color
        copy.state = this.state
        return copy
    }
}