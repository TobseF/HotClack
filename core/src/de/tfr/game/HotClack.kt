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
import de.tfr.game.lib.Logger
import de.tfr.game.lib.Logger.LogLevel
import de.tfr.game.lib.actor.Point2D
import de.tfr.game.model.GameField
import de.tfr.game.renderer.*
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
    private lateinit var timeDisplay: TimeDisplay
    private lateinit var displayRenderer: DisplayRenderer
    private lateinit var scoreRenderer: DisplayRenderer
    private lateinit var controllerRenderer: ControllerRenderer

    private lateinit var game: SimpleInfiniteGame
    private lateinit var gamepad: GamePadController
    val rings = 13
    val segments = 8 //12
    private val gameField = GameField(rings, segments)
    private val resolution = Resolution(1400f, 1400f)
    lateinit var batch: SpriteBatch
    private lateinit var stage: Stage
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var liveRenderer: LiveRenderer
    private lateinit var colorChooseRenderer: ColorChooserRenderer

    override fun create() {
        Logger.setLevel(LogLevel.Debug)
        shapeRenderer = ShapeRenderer()
        batch = SpriteBatch()
        timeDisplay = TimeDisplay()
        val colorChooser = ColorChooser()
        game = SimpleInfiniteGame(gameField, timeDisplay.watch, colorChooser)
        camera = OrthographicCamera(resolution)
        camera.setToOrtho(false)

        viewport = FitViewport(resolution.width, resolution.height, camera)
        val center = resolution.getCenter()
        renderer = GameFieldRenderer(center, gameField, camera)
        colorChooseRenderer = ColorChooserRenderer(Point2D(70f, 70f), colorChooser, newShapeRenderer(), camera)

        controller = Controller(center, renderer.getFieldRadius(), viewport, gameField.getNumberOfSegments(), colorChooseRenderer)
        controller.addTouchListener(game)
        displayRenderer = DisplayRenderer(Point2D(resolution.width - 200f, resolution.height + 50), timeDisplay::getText, camera, SpriteBatch())

        displayRenderer.init()
        scoreRenderer = DisplayRenderer(Point2D(150f, resolution.height + 50), game.scoreCounter::getText, camera, SpriteBatch())
        scoreRenderer.init()
        controllerRenderer = ControllerRenderer(camera)
        gamepad = GamePadController(gameField.getNumberOfSegments(), game)
        liveRenderer = LiveRenderer(Point2D(60f, resolution.height - 150), game::lives, newShapeRenderer(), camera)
    }

    fun newShapeRenderer(): ShapeRenderer {
        val renderer = ShapeRenderer()
        renderer.projectionMatrix = camera.combined
        return renderer
    }

    override fun render() {
        clear()
        camera.update()
        renderField()
        game.update(graphics.deltaTime)
        displayRenderer.render()
        liveRenderer.render()
    }

    private fun renderField() {
        with(renderer) {
            start()
            render()
            render(game.skyNet)
            game.getStones().forEach(renderer::renderStone)
            scoreRenderer.render()
            colorChooseRenderer.render()
            end()
        }
    }

    private fun clear() = clear(Color.BLACK)

    private fun clear(color: Color) {
        Gdx.gl.glClearColor(color)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or
                //Hack to also support NVIDIA Tegra Chips (http://www.badlogicgames.com/wordpress/?p=2071)
                GL20.GL_DEPTH_BUFFER_BIT or if (Gdx.graphics.bufferFormat.coverageSampling) GL20.GL_COVERAGE_BUFFER_BIT_NV else 0)

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