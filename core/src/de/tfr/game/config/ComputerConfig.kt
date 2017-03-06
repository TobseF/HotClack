package de.tfr.game.config

import de.tfr.game.lib.actor.Point
import de.tfr.game.lib.actor.Point2D

/**
 * @author Tobse4Git@gmail.com
 */
class ComputerConfig() : GameConfig(Resolution(1400f, 1400f), EntityConf(ColorChooserRendererConf(position = Point2D(70f, 70f), radius = 50f, gap = 6f, selectionWidth = 8f, selectionGap = 8f)))
