package de.tfr.game.lib

import com.badlogic.gdx.Gdx
import kotlin.reflect.KClass

/**
 * @author Tobse4Git@gmail.com
 */
class Logger(val tag: String) : Log {

    companion object {
        fun new(classForTag: KClass<*>): Log = Logger(classForTag)
    }

    constructor(classForTag: KClass<*>) : this(classForTag.simpleName ?: "")

    /** Logs a message to the console or logcat  */
    override fun log(message: String) = Gdx.app.log(tag, message)

    /** Logs a message to the console or logcat  */
    override fun log(message: String, exception: Throwable) = Gdx.app.log(tag, message, exception)

    /** Logs an error message to the console or logcat  */
    override fun error(message: String) = Gdx.app.error(tag, message)

    /** Logs an error message to the console or logcat  */
    override fun error(message: String, exception: Throwable) = Gdx.app.error(tag, message, exception)

    /** Logs a debug message to the console or logcat  */
    override fun debug(message: String) = Gdx.app.debug(tag, message)

    /** Logs a debug message to the console or logcat  */
    override fun debug(message: String, exception: Throwable) = Gdx.app.debug(tag, message, exception)

}