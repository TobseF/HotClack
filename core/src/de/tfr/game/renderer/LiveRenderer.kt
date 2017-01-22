package de.tfr.game.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import de.tfr.game.lib.actor.Point

/**
 * @author Tobse4Git@gmail.com
 *
 *
 */
class LiveRenderer(point: Point, val liveProvider: () -> Int, val renderer: ShapeRenderer, val camera: Camera) : Point by point {

    fun render() {
        renderer.projectionMatrix = camera.combined
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        val lives = liveProvider.invoke()
        for (i in 0..(lives - 1)) {
            renderer.color = Color.WHITE
            val radius = 40f
            val gap = 8f
            renderer.circle(x + (i * ((radius * 2) + gap)), y, radius)
        }

        renderer.end()
    }

}