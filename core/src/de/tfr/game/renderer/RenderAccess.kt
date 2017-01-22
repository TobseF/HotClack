package de.tfr.game.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
 * @author Tobse4Git@gmail.com
 */
class RenderAccess(camera: Camera) {

    val shape = ShapeRenderer()
    val sprite = SpriteBatch()

    init {
        shape.projectionMatrix = camera.combined
        sprite.projectionMatrix = camera.combined
        shape.setAutoShapeType(true)
        //shape.begin(ShapeRenderer.ShapeType.Filled)
        shape.begin()
    }
}
