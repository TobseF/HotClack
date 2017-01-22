package de.tfr.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.tfr.game.controller.GamePadController
import de.tfr.game.lib.actor.Point2D
import de.tfr.game.model.GameField
import de.tfr.game.renderer.ControllerRenderer
import de.tfr.game.renderer.DisplayRenderer
import de.tfr.game.renderer.GameFieldRenderer
import de.tfr.game.renderer.RenderAccess
import de.tfr.game.util.extensions.OrthographicCamera
import de.tfr.game.util.extensions.glClearColor

/**
 * @author Tobse4Git@gmail.com
 */
class HotClack : ApplicationAdapter() {

    data class Resolution(var width: Float, var height: Float) {
        fun getCenter() = Point2D(width / 2, height / 2)
    }

    private lateinit var camera: OrthographicCamera
    private lateinit var renderer: GameFieldRenderer
    private lateinit var controller: Controller
    private lateinit var viewport: Viewport
    private lateinit var display: TimeDisplay
    private lateinit var displayRenderer: DisplayRenderer
    private lateinit var scoreRenderer: DisplayRenderer
    private lateinit var controllerRenderer: ControllerRenderer

    private lateinit var game: SimpleInfiniteGame
    private lateinit var gamepad: GamePadController

    private val gameField = GameField(13)
    private val resolution = Resolution(1400f, 1400f)
    lateinit var batch: SpriteBatch
    private lateinit var stage: Stage
    private lateinit var shapeRenderer: ShapeRenderer

    override fun create() {
        shapeRenderer = ShapeRenderer()
        batch = SpriteBatch()
        game = SimpleInfiniteGame(gameField)
        camera = OrthographicCamera(resolution)
        camera.setToOrtho(false)
        val renderAccess = RenderAccess(camera)

        viewport = FitViewport(resolution.width, resolution.height, camera)
        val center = resolution.getCenter()
        renderer = GameFieldRenderer(center, camera)
        val gameFieldSize = renderer.getFieldSize(gameField)
        controller = Controller(center, gameFieldSize, viewport)
        controller.addTouchListener(game)
        display = TimeDisplay()
        displayRenderer = DisplayRenderer(Point2D(resolution.width - 200f, resolution.height + 50), display::getText, camera, SpriteBatch())

        displayRenderer.init()
        scoreRenderer = DisplayRenderer(Point2D(150f, resolution.height + 50), game.scoreCounter::getText, camera, SpriteBatch())
        scoreRenderer.init()
        controllerRenderer = ControllerRenderer(camera)
        gamepad = GamePadController(gameField.segments(), game)

    }

    override fun render() {
        clear()
        camera.update()
        // controllerRenderer.render(controller)
        renderField()
        game.update(graphics.deltaTime)
        displayRenderer.render()
    }

    private fun renderField() {
        with(renderer) {
            start()
            render(game.field)
            render(game.skyNet)
            game.getStones().forEach(renderer::renderStone)
            scoreRenderer.render()
            end()
        }
    }

    private fun clear() = clear(Color.BLACK)

    private fun clear(color: Color) {
        Gdx.gl.glClearColor(color)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        //renderGameBackground()
    }

    private fun renderGameBackground() {
        shapeRenderer.setAutoShapeType(true)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.rect(0f, 0f, resolution.width, resolution.height)
        shapeRenderer.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }
}