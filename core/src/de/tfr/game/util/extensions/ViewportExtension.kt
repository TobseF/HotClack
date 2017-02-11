package de.tfr.game.util.extensions

import com.badlogic.gdx.utils.viewport.Viewport

/**
 * @author Tobse4Git@gmail.com
 */

fun Viewport.unproject(screenX: Int, screenY: Int) = this.unproject(Vector2(screenX, screenY))