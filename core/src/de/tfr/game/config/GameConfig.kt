package de.tfr.game.config

import de.tfr.game.lib.actor.Point
import de.tfr.game.lib.actor.Point2D

/**
 * @author Tobse4Git@gmail.com
 */
open class GameConfig(val resolution: Resolution, val entityConf: GameConfig.EntityConf) {


    class EntityConf(val colorChooserRenderer: ColorChooserRendererConf) {

    }

    class ColorChooserRendererConf(
            val position: Point,
            val radius: Float,
            val gap: Float,
            val selectionWidth: Float,
            val selectionGap: Float
    )


    data class Resolution(var width: Float, var height: Float) {
        fun getCenter(): Point = Point2D(width / 2, height / 2)
    }
}