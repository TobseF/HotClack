package de.tfr.game.config

import de.tfr.game.lib.actor.Point
import de.tfr.game.lib.actor.Point2D

/**
 * @author Tobse4Git@gmail.com
 */
class AndroidConfig() : GameConfig(Resolution(1400f, 1800f), EntityConf(ColorChooserRendererConf(position = Point2D(435f, 80f), radius = 80f, gap = 16f, selectionWidth = 8f, selectionGap = 8f)))