package de.tfr.game

import de.tfr.game.Controller.ControlEvent.ControlEvenType.Clicked
import de.tfr.game.Controller.ControlEvent.ControlEvenType.Selected
import de.tfr.game.lib.Logger
import de.tfr.game.model.GameField
import de.tfr.game.model.Ring
import de.tfr.game.model.Stone
import de.tfr.game.util.StopWatch
import de.tfr.game.util.Timer

/**
 * @author Tobse4Git@gmail.com
 */
class SimpleInfiniteGame(val field: GameField, val watch: StopWatch, private val colorChooser: ColorChooser) : Controller.ControlListener {

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

    override fun controlEvent(controlEvent: Controller.ControlEvent) {
        if (looseLive == null) {
            if (controlEvent.type == Clicked) {
                when (controlEvent.control) {
                Controller.Control.Blue -> shootColor(Stone.Color.Blue)
                Controller.Control.Red -> shootColor(Stone.Color.Red)
                Controller.Control.Yellow -> shootColor(Stone.Color.Yellow)
                Controller.Control.Green -> shootColor(Stone.Color.Green)
                    Controller.Control.Up -> setColor(colorChooser.next())
                    Controller.Control.Down -> setColor(colorChooser.prev())
                Controller.Control.Left -> moveLeft()
                Controller.Control.Right -> moveRight()

                    Controller.Control.Action -> shootColor()
                Controller.Control.Esc -> reset()
                Controller.Control.Pause -> gogglePause()
            }
            } else if (controlEvent.type == Selected) {
                controlEvent.control.toColor()?.let(this::setColor)
            }
        }
    }

    private fun setColor(color: Stone.Color) {
        player.color = color
        colorChooser.color = color
    }


    private fun gogglePause() {
        timer.togglePause()
        watch.togglePause()
    }

    fun moveLeft() {
        var segment = player.block.segment - 1
        if (segment < 0) {
            segment = field.getNumberOfSegments() - 1
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

    fun shootColor(color: Stone.Color? = null) {
        player.color = color ?: player.color

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

    override fun controlEventSetSegment(type: Controller.SegmentActionType, segment: Int) {
        if (segment in 0 until field.getNumberOfSegments()) {
            val next = field[player.block.row][segment]
            player.block = next
        }
        if (type == Controller.SegmentActionType.Clicked) {
            shootColor()
        }
    }

}
