package de.tfr.game.lib

/**
 * @author Tobse4Git@gmail.com
 */
interface Log {
    /** Logs a message to the console or logcat  */
    fun log(message: String)

    /** Logs a message to the console or logcat  */
    fun log(message: String, exception: Throwable)

    /** Logs an error message to the console or logcat  */
    fun error(message: String)

    /** Logs an error message to the console or logcat  */
    fun error(message: String, exception: Throwable)

    /** Logs a debug message to the console or logcat  */
    fun debug(message: String)

    /** Logs a debug message to the console or logcat  */
    fun debug(message: String, exception: Throwable)
}