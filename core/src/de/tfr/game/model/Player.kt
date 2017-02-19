package de.tfr.game.model

/**
 * @author Tobse4Git@gmail.com
 */
class Player(initBlock: Block, color: Color) : Stone(initBlock, color) {
    var blocked = false
}