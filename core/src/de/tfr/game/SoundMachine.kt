package de.tfr.game

import com.badlogic.gdx.Gdx.audio
import com.badlogic.gdx.Gdx.files

/**
 * @author Tobse4Git@gmail.com
 */
class SoundMachine() {

    private val enabled = true

    private val circle_ok = newSound("circle_ok.ogg")
    private val line_missed = newSound("line_missed.ogg")
    private val line_ok = newSound("line_ok.ogg")

    private fun newSound(fileName: String) = audio.newSound(files.internal("sounds/" + fileName))

    fun playCircleOK() {
        if (enabled) {
            circle_ok.play()
        }
    }

    fun playLineMissed() {
        if (enabled) {
            line_missed.play()
        }
    }

    fun playLineOK() {
        if (enabled) {
            line_ok.play()
        }
    }
}