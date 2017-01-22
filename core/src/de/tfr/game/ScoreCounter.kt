package de.tfr.game

/**
 * @author Tobse4Git@gmail.com
 */
class ScoreCounter(var score: Long = 0) {

    val penaltyScore = 3

    fun getText() = String.format("%05d", score)

    fun score() = score++

    fun unScore() {
        score = Math.max(0, score - penaltyScore)
    }

    fun reset() {
        score = 0
    }

}
