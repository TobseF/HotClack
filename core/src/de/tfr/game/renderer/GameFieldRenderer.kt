package de.tfr.game.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import de.tfr.game.lib.actor.Point
import de.tfr.game.lib.actor.Point2D
import de.tfr.game.model.Block
import de.tfr.game.model.GameField
import de.tfr.game.model.Ring
import de.tfr.game.model.Stone
import de.tfr.game.ui.GREEN_LIGHT


/**
 * @author Tobse4Git@gmail.com
 */
class GameFieldRenderer(point: Point, val camera: Camera) : Point by point {

    private val gap = 14
    private val blockWith = 36f
    private val radius = 18f
    private val radiusStoned = radius + 2
    private val radiusPlayer = radiusStoned + 5
    private val renderer = ShapeRenderer()
    private val spriteBatch = SpriteBatch()

    class Colors {
        companion object {
            val emptyField = Color.CYAN
            val stoneRed = Color.RED
            val stoneBlue = Color.BLUE
            val stoneYellow = Color.YELLOW
            val stoneGreen = Color.GREEN

            val activeStone = Color.CYAN
            val setStone = Color.CYAN
        }
    }

    //private val background = Texture()

    fun start() {
        val width = Gdx.graphics.width
        val height = Gdx.graphics.height

        //val frameBuffer = FrameBuffer(Pixmap.Format.RGB565, width, height, false);
        // val frameRegion = TextureRegion(frameBuffer.getColorBufferTexture());
        //  frameRegion.flip(false, true);
        renderer.projectionMatrix = camera.combined
        renderer.begin(Filled)
    }

    fun render(field: GameField) {

        renderBackground(field)

        renderer.color = Colors.emptyField
        renderer.circle(x, y, radius)
        field.forEach(this::renderRing)
        renderPlayer(field.player)
        // renderBorder()
    }

    private fun renderPlayer(player: Stone) {
        val playerPos = getPos(player.block)
        renderer.color = Color.BLACK
        renderer.circle(playerPos.x, playerPos.y, radiusPlayer + 4)
        renderer.color = getRenderColor(player.color)
        renderer.circle(playerPos.x, playerPos.y, radiusPlayer + 4)

    }

    fun renderBorder() {
        spriteBatch.begin()
        spriteBatch.enableBlending()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_BLEND_DST_ALPHA)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_BLEND_DST_ALPHA)

        spriteBatch.projectionMatrix = camera.combined
        //  spriteBatch.draw(background, x - (background.height / 2), y - (background.width / 2))
        spriteBatch.end()
    }

    private fun renderBackground(field: GameField) {
        renderer.color = GREEN_LIGHT
        val radius = getFieldSize(field)
        renderer.rect(x - radius, y - radius, radius * 2, radius * 2)
    }

    fun getFieldSize(field: GameField): Float = (blockWith / 2) + field.size * (gap + blockWith)

    fun end() {
        renderer.end()
        //spriteBatch.end()
    }

    private fun renderRing(ring: Ring) {
        ring.forEach { renderBlock(it, it.stone) }
    }

    fun renderTouchArea(touchAreas: List<Rectangle>) {
        renderer.color = Color.NAVY

        touchAreas.forEach {
            val center = it.getCenter(Vector2())
            renderer.circle(center.x, center.y, it.width / 2)
        }
    }

    fun renderStone(stone: Stone) {
        renderBlock(stone.block, stone)
    }

    private fun getPos(block: Block): Point {
        val distance = gap + blockWith + (block.row * (gap + blockWith))
        val singleRotation = (360 / 12).toFloat()
        val degree = block.segment * singleRotation
        val x = this.x + (distance * Math.sin(Math.toRadians(degree.toDouble()))).toFloat()
        val y = this.y + (distance * Math.cos(Math.toRadians(degree.toDouble()))).toFloat()
        return Point2D(x, y)
    }

    fun getRenderColor(color: Stone.Color): Color {
        when (color) {
            Stone.Color.Blue -> return Colors.stoneBlue
            Stone.Color.Green -> return Colors.stoneGreen
            Stone.Color.Yellow -> return Colors.stoneYellow
            Stone.Color.Red -> return Colors.stoneRed
            Stone.Color.Undefined -> return Colors.emptyField
        }
    }

    private fun renderBlock(block: Block, stone: Stone?) {
        val renderPos = getPos(block)

        if (stone != null) {
            renderer.color = getRenderColor(stone.color)
            renderer.circle(renderPos.x, renderPos.y, radiusStoned)
        } else {
            renderer.color = Colors.emptyField
            renderer.circle(renderPos.x, renderPos.y, radius)
        }

        when {
        /* stone == null -> renderer.color = Colors.emptyField
         stone.state == Stone.State.Active -> renderer.color = BLACK
         stone.state == Stone.State.Set -> renderer.color = GRAY_DARK*/
        }

        // renderer.arc(x,y,5f,distance,1*singleRotation,12)
        //  renderBlock(block, block.segment * singleRotation, distance, stone)
        for (i in 0..12) {
            //renderBlock(block, i*singleRotation, distance, stone)
        }
    }

    private fun renderBlock(block: Block, degree: Float, distance: Float, stone: Stone?) {

        //renderBlock(block, stone, x, y + distance, degree)
    }


    private fun renderBlock(block: Block, stone: Stone?, x: Float, y: Float, degree: Float) {
        val lenghGrowth = 20f;
        val length = lenghGrowth + (block.row * lenghGrowth)
        val side = length / 2
        val width = blockWith / 2
        val smallSide = 5f


        //(float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY,float degrees)
        //renderer.rect( x - side, y - width, this.x, this.y,length, blockWith,1f,1f,degree)
        renderer.rect(x - side, y - width, x - side, y - width, length, blockWith, 1f, 1f, degree)
        renderer.rect(x - side, y - width, length, blockWith)
        /*
        when (block.orientation) {
            Orientation.Left -> renderer.rect(x - width, y - side, blockWith, length)
            Orientation.Right -> renderer.rect(x - width, y - side, blockWith, length)
            Orientation.Up -> renderer.rect(x - side, y - width, length, blockWith)
            Orientation.Down -> renderer.rect(x - side, y - width, length, blockWith)
        }

        fun renderTriangleLeftUp(x: Float, y: Float) = renderer.triangle(x, y, x, y + blockWith, x + blockWith, y)
        fun renderTriangleLeftDown(x: Float, y: Float) = renderer.triangle(x, y, x, y - blockWith, x + blockWith, y)
        fun renderTriangleRightUp(x: Float, y: Float) = renderer.triangle(x, y, x + blockWith, y + blockWith, x + blockWith, y)
        fun renderTriangleRightDown(x: Float, y: Float) = renderer.triangle(x, y, x + blockWith, y - blockWith, x + blockWith, y)
        fun renderTriangleUpLeft(x: Float, y: Float) = renderer.triangle(x, y, x, y + blockWith, x - smallSide, y + blockWith)
        fun renderTriangleUpRight(x: Float, y: Float) = renderer.triangle(x, y, x, y + blockWith, x + smallSide, y + blockWith)
        fun renderTriangleDownLeft(x: Float, y: Float) = renderer.triangle(x, y, x, y + blockWith, x - blockWith, y)
        fun renderTriangleDownRight(x: Float, y: Float) = renderer.triangle(x, y, x, y + blockWith, x + blockWith, y)

        when (block.orientation) {
            Orientation.Left -> {
                renderTriangleLeftUp(x - width, y + side)
                renderTriangleLeftDown(x - width, y - side)
            }
            Orientation.Right -> {
                renderTriangleRightUp(x - width, y + side)
                renderTriangleRightDown(x - width, y - side)
            }
            Orientation.Up -> {
                renderTriangleUpLeft(x - side, y - width)
                renderTriangleUpRight(x + side, y - width)
            }
            Orientation.Down -> {
                renderTriangleDownLeft(x - side, y - width)
                renderTriangleDownRight(x + side, y - width)
            }

        }
        */
    }

}