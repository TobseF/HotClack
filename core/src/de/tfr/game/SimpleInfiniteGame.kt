package de.tfr.game

import de.tfr.game.model.GameField
import de.tfr.game.model.Orientation
import de.tfr.game.model.Ring
import de.tfr.game.model.Stone
import de.tfr.game.model.Stone.State
import de.tfr.game.util.Timer

/**
 * @author Tobse4Git@gmail.com
 */
class SimpleInfiniteGame(val field: GameField) : Controller.ControlListener {

    override fun controlEventSetSegment(segment: Int) {
        if (segment < field.segments()) {
            val next = field[player.block.row][segment]
            player.block = next
        }
    }

    private var player: Stone
    private var activeRing: Ring? = null
    private val timer: Timer
    private val fallingSpeed = 0.3f
    private val firstPause = 0.7f
    private val sounds = SoundMachine()
    val skyNet: EnemyAI

    init {
        player = field.player
        timer = Timer(firstPause, this::doStep)
        skyNet = EnemyAI(field)
    }

    private fun doStep() {
        timer.actionTime = fallingSpeed
        skyNet.doStep()
        // move(active)
    }

    override fun controlEvent(control: Controller.Control) {
        when (control) {
        // active.block.orientation.toControl() -> setStone()
            Controller.Control.Blue -> setColor(Stone.Color.Blue)
            Controller.Control.Red -> setColor(Stone.Color.Red)
            Controller.Control.Yellow -> setColor(Stone.Color.Yellow)
            Controller.Control.Green -> setColor(Stone.Color.Green)
            Controller.Control.Up -> moveUp()
            Controller.Control.Down -> moveDown()
            Controller.Control.Action -> setStone(player)
            Controller.Control.Esc -> reset()
            Controller.Control.Pause -> timer.togglePause()
        }
    }

    private fun moveUp() {
        val nextRow = player.block.row - 1
        if (nextRow >= 0) {
            val nextBlock = field[nextRow][player.block.segment]
            player.block = nextBlock
        }
    }

    private fun dropStone() {
        val dropped = player.clone()
        dropped.state = State.Wall
        player.block.stone = dropped
    }

    private fun moveDown() {
        val nextRow = player.block.row + 1
        if (nextRow < field.size) {
            val next = field[nextRow][player.block.segment]
            player.block = next
        }
    }

    fun setColor(color: Stone.Color) {
        player.color = color
        skyNet.shoot(player.block.segment)
        // dropStone()
    }


    fun getStones() = listOf(player)

    fun update(deltaTime: Float) {
        timer.update(deltaTime)
        skyNet.update(deltaTime)
    }

    private fun reset() {
        field.reset()
        timer.reset()
        activeRing = null
        respawnStone()
    }

    private fun respawnStone() {
        val field = field[field.size - 1][randomFreeOrientation()]
        player = Stone(field, Stone.Color.Red)
        timer.reset()
        timer.actionTime = firstPause
    }

    private fun randomFreeOrientation(): Orientation {
        //activeRing?.randomFreeSide() ?: Orientation.random()
        return Orientation.Up
    }


    private fun move(stone: Stone) {
        if (stone.isInLastRow()) {
            misstep()
        } else {
            val next = field[player.block.row - 1][player.block.segment]
            player.block = next
        }
    }

    private fun Stone.isInLastRow() = this.block.row == 0

    private fun setStone() {
        if (player.block.isEmpty()) {
            setStone(player)
        }
    }

    private infix fun Stone.isOutsideOf(ring: Ring?) = ring?.index != this.block.row

    private fun setStone(stone: Stone) {
        player.block.stone = player
        stone.freeze()
        if (activeRing != null) {
            if (activeRing!!.isFull()) {
                sounds.playCircleOK()
                activeRing = null
            } else if (player isOutsideOf activeRing) {
                misstep()
            } else {
                sounds.playLineOK()
            }
        } else {
            sounds.playLineOK()
            activeRing = field[player.block.row]
        }
        respawnStone()
    }

    private fun misstep() {
        sounds.playLineMissed()
        resetRing()
        if (player.state == State.Set) {
            player.block.reset()
        }
        respawnStone()
    }

    private fun resetRing() {
        activeRing?.reset()
        activeRing = null
        resetLastFullRing()
    }

    private fun firstFull() = field.find(Ring::isFull)

    private fun resetLastFullRing() {
        firstFull()?.reset()
    }

}
