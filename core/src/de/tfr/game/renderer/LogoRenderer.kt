package de.tfr.game.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import de.tfr.game.lib.actor.Point

/**
 * @author Tobse4Git@gmail.com
 */
class LogoRenderer(point: Point, val camera: Camera, val spriteBatch: SpriteBatch, val gameFieldSize: Float) : Point by point {
    var logo: Texture

    init {
        logo = Texture(Gdx.files.internal(""))
        logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    fun render() {
        spriteBatch.projectionMatrix = camera.combined
        spriteBatch.begin()
        spriteBatch.draw(logo, x - 200, y + gameFieldSize + 250f)
        spriteBatch.end()
    }
}