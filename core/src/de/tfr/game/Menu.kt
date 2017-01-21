package de.tfr.game

/**
 * @author Tobse4Git@gmail.com
 */
class Menu : Controller.ControlListener {
    override fun controlEventSetSegment(segment: Int) {

    }

    enum class Game {BoxGame, BlockGame }

    var level: Int = 0

    fun levelUp() = level++

    fun levelDown() {
        if (level > 0) {
            level--
        }
    }

    override fun controlEvent(control: Controller.Control) {
    }

}
