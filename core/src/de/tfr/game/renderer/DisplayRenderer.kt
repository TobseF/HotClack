package de.tfr.game.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import de.tfr.game.lib.actor.Point
import de.tfr.game.ui.GREEN_LIGHT

/**
 * @author Tobse4Git@gmail.com
 */
class DisplayRenderer(val display: Point, val textProvider: () -> String, val camera: Camera, val batch: SpriteBatch) {
    lateinit var font: BitmapFont

    lateinit var glyphLayout: GlyphLayout
    private var renderer = ShapeRenderer()

    fun init() {
        font = BitmapFont(Gdx.files.internal("fonts/segment7.fnt"))
        glyphLayout = GlyphLayout(font, "0000")
    }

    fun render() {

        renderer.projectionMatrix = camera.combined
        renderer.begin(ShapeRenderer.ShapeType.Filled)

        renderer.color = GREEN_LIGHT

        renderer.end()

        batch.projectionMatrix = camera.combined
        batch.begin()
        glyphLayout.setText(font, "8888")
        font.color = Color.WHITE
        font.draw(batch, glyphLayout, display.x - (glyphLayout.width / 2), display.y - glyphLayout.height / 2)
        glyphLayout.setText(font, textProvider.invoke())
        font.color = Color.DARK_GRAY
        font.draw(batch, glyphLayout, display.x - (glyphLayout.width / 2), display.y - glyphLayout.height / 2)
        batch.end()
    }


}
