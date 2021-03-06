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
import de.tfr.game.config.GameConfig
import de.tfr.game.controller.GamePadController
import de.tfr.game.lib.actor.Point2D
import de.tfr.game.model.GameField
import de.tfr.game.renderer.*
import de.tfr.game.util.extensions.OrthographicCamera
import de.tfr.game.util.extensions.glClearColor
import de.tfr.game.util.extensions.FitViewport

/**
 * @author Tobse4Git@gmail.com
 */
class HotClack(val config: GameConfig) : ApplicationAdapter() {

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
    lateinit var batch: SpriteBatch
    private lateinit var stage: Stage
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var liveRenderer: LiveRenderer
    private lateinit var colorChooseRenderer: ColorChooserRenderer

    override fun create() {
        //Logger.setLevel(LogLevel.Debug)
        shapeRenderer = ShapeRenderer()
        batch = SpriteBatch()
        timeDisplay = TimeDisplay()
        val colorChooser = ColorChooser()
        game = SimpleInfiniteGame(gameField, timeDisplay.watch, colorChooser)
        camera = OrthographicCamera(config.resolution)
        camera.setToOrtho(false)

        viewport = FitViewport(config.resolution, camera)
        val center = config.resolution.getCenter()
        renderer = GameFieldRenderer(center, gameField, camera)
        colorChooseRenderer = ColorChooserRenderer(config.entityConf.colorChooserRenderer, colorChooser, newShapeRenderer(), camera)

        controller = Controller(center, renderer.getFieldRadius(), viewport, gameField.getNumberOfSegments(), colorChooseRenderer)
        controller.addTouchListener(game)
        displayRenderer = DisplayRenderer(Point2D(config.resolution.width - 200f, config.resolution.height + 50), timeDisplay::getText, camera, SpriteBatch())

        displayRenderer.init()
        scoreRenderer = DisplayRenderer(Point2D(150f, config.resolution.height + 50), game.scoreCounter::getText, camera, SpriteBatch())
        scoreRenderer.init()
        controllerRenderer = ControllerRenderer(camera)
        gamepad = GamePadController(gameField.getNumberOfSegments(), game)
        liveRenderer = LiveRenderer(Point2D(60f, config.resolution.height - 150), game::lives, newShapeRenderer(), camera)
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
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }
}