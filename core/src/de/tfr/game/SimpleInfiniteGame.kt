package de.tfr.game

import de.tfr.game.lib.Logger
import de.tfr.game.model.GameField
import de.tfr.game.model.Ring
import de.tfr.game.model.Stone
import de.tfr.game.model.Stone.State
import de.tfr.game.util.StopWatch
import de.tfr.game.util.Timer

/**
 * @author Tobse4Git@gmail.com
 */
class SimpleInfiniteGame(val field: GameField, val watch: StopWatch) : Controller.ControlListener {

    companion object {
        val log = Logger.new(SimpleInfiniteGame::class)
    }

    private var player: Stone
    private var activeRing: Ring? = null
    private val timer: Timer
    private var looseLive: Timer? = null
    private val incomingSpeedStart = 0.1f
    private var incomingSpeedMax = 0.1f
    private val firstPause = 0.01f
    private val sounds = SoundMachine()
    val scoreCounter = ScoreCounter()

    val skyNet: EnemyAI

    var lives = 3

    init {
        player = field.player
        timer = Timer(firstPause, this::doStep)
        skyNet = EnemyAI(field, this::enemyReachedBase)
    }

    fun enemyReachedBase() {
        looseLive()
    }

    private fun doStep(deltaTime: Float) {
        timer.actionTime = incomingSpeedMax

    }

    override fun controlEvent(control: Controller.Control) {
        if (looseLive == null) {
            when (control) {
                Controller.Control.Blue -> shootColor(Stone.Color.Blue)
                Controller.Control.Red -> shootColor(Stone.Color.Red)
                Controller.Control.Yellow -> shootColor(Stone.Color.Yellow)
                Controller.Control.Green -> shootColor(Stone.Color.Green)
                Controller.Control.Up -> moveUp()
                Controller.Control.Down -> moveDown()
                Controller.Control.Left -> moveLeft()
                Controller.Control.Right -> moveRight()

                Controller.Control.Action -> setStone(player)
                Controller.Control.Esc -> reset()
                Controller.Control.Pause -> gogglePause()
            }
        }
    }

    private fun gogglePause() {
        timer.togglePause()
        watch.togglePause()
    }

    fun moveLeft() {
        var segment = player.block.segment - 1
        if (segment < 0) {
            segment = field.getNumberOfSegments()
        }
        player.block = field[player.block.row][segment]
    }

    fun moveRight() {
        var segment = player.block.segment + 1
        if (segment >= field.getNumberOfSegments()) {
            segment = 0
        }
        player.block = field[player.block.row][segment]
    }

    private fun moveUp() {
        val nextRow = player.block.row - 1
        if (nextRow >= 0) {
            val nextBlock = field[nextRow][player.block.segment]
            player.block = nextBlock
        }
    }

    private fun moveDown() {
        val nextRow = player.block.row + 1
        if (nextRow < field.getNumberOfSegments()) {
            val next = field[nextRow][player.block.segment]
            player.block = next
        }
    }

    fun shootColor(color: Stone.Color) {
        player.color = color

        val enemy = skyNet.getEnemy(player.block.segment)
        if (enemy != null && enemy.color == player.color) {
            skyNet.shoot(enemy)
            sounds.playLineOK()
            scoreCounter.score()
        } else {
            scoreCounter.unScore()
            if (looseLive == null) {
                looseLive()
            }
        }
    }

    private fun looseLive() {
        lives--
        skyNet.pause()
        if (lives == 0) {
            sounds.playGameOver()
            looseLive = Timer(5f, this::gameOver)
        } else {
            sounds.playLineMissed()
            looseLive = Timer(2f, this::killAll)
        }

    }

    private fun gameOver() {
        looseLive?.actionTime = 5f
        reset()
    }

    fun killAll() {
        looseLive = null
        skyNet.resume()
        skyNet.killAll()
    }


    fun getStones() = listOf(player)

    fun update(deltaTime: Float) {
        timer.update(deltaTime)
        if (!watch.pause) {
            skyNet.update(deltaTime)
            looseLive?.update(deltaTime)
        }
    }

    private fun reset() {
        lives = 3
        scoreCounter.reset()
        field.reset()
        timer.reset()
        skyNet.reset()
        activeRing = null
        looseLive = null
        watch.reset()
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
    }

    override fun controlEventSetSegment(segment: Int) {
        if (segment in 0 until field.getNumberOfSegments()) {
            val next = field[player.block.row][segment]
            player.block = next
        } else {

        }
    }


    private fun misstep() {
        sounds.playLineMissed()
        resetRing()
        if (player.state == State.Set) {
            player.block.reset()
        }
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
