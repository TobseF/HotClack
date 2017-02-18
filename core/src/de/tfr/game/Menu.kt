package de.tfr.game

/**
 * @author Tobse4Git@gmail.com
 */
class Menu : Controller.ControlListener {

    enum class Game {BoxGame, BlockGame }

    var level: Int = 0

    fun levelUp() = level++

    fun levelDown() {
        if (level > 0) {
            level--
        }
    }

    override fun controlEventSetSegment(type: Controller.SegmentActionType, segment: Int) {}
    override fun controlEvent(controlEvent: Controller.ControlEvent) {}

}
