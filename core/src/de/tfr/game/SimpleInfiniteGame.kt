package de.tfr.game

import com.badlogic.gdx.Gdx
import de.tfr.game.Controller.ControlEvent.ControlEvenType.Clicked
import de.tfr.game.Controller.ControlEvent.ControlEvenType.Selected
import de.tfr.game.lib.Logger
import de.tfr.game.model.GameField
import de.tfr.game.model.Player
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

    private var player: Player
    private var activeRing: Ring? = null
    private var missedShotPause: Timer = Timer(2.5F, this::reactivatePlayer, runOnStart = false, infinite = false)
    private var looseLivePause: Timer = Timer(2.5F, this::reactivatePlayer, runOnStart = false, infinite = false)
    private var gameOverPause: Timer = Timer(5f, this::gameOver, runOnStart = false, infinite = false)
    private var resetPause: Timer = Timer(2f, this::reset, runOnStart = false, infinite = false)
    private val livesAtStart = 3
    private val sounds = SoundMachine()
    private val vibrateTime = 32
    val scoreCounter = ScoreCounter()

    val skyNet: EnemyAI

    var lives = livesAtStart

    init {
        player = field.player
        skyNet = EnemyAI(field, this::enemyReachedBase)
    }

    fun enemyReachedBase() {
        looseLive()
    }

    override fun controlEvent(controlEvent: Controller.ControlEvent) {
        if (isPlayerBlocked()) {
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
        watch.togglePause()
    }

    fun moveLeft() {
        var segment = player.block.segment - 1
        if (segment < 0) {
            segment = field.getNumberOfSegments() - 1
        }
        player.block.segment = field[player.block.row][segment].segment
    }

    fun moveRight() {
        var segment = player.block.segment + 1
        if (segment >= field.getNumberOfSegments()) {
            segment = 0
        }
        player.block.segment = field[player.block.row][segment].segment
    }

    fun shootColor(color: Stone.Color? = null) {
        player.color = color ?: player.color

        val enemy = skyNet.getEnemy(player.block.segment)
        if (enemy != null && enemy.color == player.color) {
            skyNet.shoot(enemy)
            sounds.playLineOK()
            scoreCounter.score()
        } else {
            missedShot()
        }
        Gdx.input.vibrate(vibrateTime)
    }

    private fun missedShot() {
        blockPlayer()
        sounds.playLineMissed()
        missedShotPause.reStart()
    }

    private fun reactivatePlayer() {
        player.blocked = false
        colorChooser.blocked = false
    }

    private fun looseLive() {
        lives--
        skyNet.pause()
        sounds.playGameOver()
        blockPlayer()
        if (lives == 0) {
            watch.pause()
            gameOverPause.reStart()
        } else {
            killAll()
            looseLivePause.reStart()
        }
    }

    private fun blockPlayer() {
        player.blocked = true
        colorChooser.blocked = true
    }

    private fun gameOver() {
        resetPause.reStart()
    }

    fun killAll() {
        skyNet.resume()
        skyNet.killAll()
    }

    fun getStones() = listOf(player)

    fun update(deltaTime: Float) {
        if (!watch.pause) {
            skyNet.update(deltaTime)
        }
        looseLivePause.update(deltaTime)
        missedShotPause.update(deltaTime)
        gameOverPause.update(deltaTime)
        resetPause.update(deltaTime)
    }

    private fun reset() {
        lives = livesAtStart
        scoreCounter.reset()
        field.reset()
        skyNet.reset()
        activeRing = null
        missedShotPause.reset()
        gameOverPause.reset()
        resetPause.reset()
        reactivatePlayer()
        watch.reset()
    }

    override fun controlEventSetSegment(type: Controller.SegmentActionType, segment: Int) {
        if (isPlayerBlocked()) {
            if (segment in 0 until field.getNumberOfSegments()) {
                val next = field[player.block.row][segment]
                player.block.segment = next.segment
            }
            if (type == Controller.SegmentActionType.Clicked) {
                shootColor()
            }
        }
    }

    private fun isPlayerBlocked() = !(missedShotPause.isRunning() || looseLivePause.isRunning() || gameOverPause.isRunning())

}
