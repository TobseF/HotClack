package de.tfr.game

/**
 * @author Tobse4Git@gmail.com
 */
class ScoreCounter(var score: Long = 0) {

    fun getText() = String.format("%05d", score)

    fun score() = score++
}
