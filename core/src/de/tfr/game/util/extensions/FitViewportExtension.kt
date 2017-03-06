package de.tfr.game.util.extensions

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.utils.viewport.FitViewport
import de.tfr.game.config.GameConfig

/**
 * @author Tobse4Git@gmail.com
 */

fun FitViewport(resolution: GameConfig.Resolution, camera: Camera) = FitViewport(resolution.width, resolution.height, camera)
