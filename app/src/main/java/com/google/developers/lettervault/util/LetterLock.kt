package com.google.developers.lettervault.util

import android.util.Base64

/**
 * Control the encode and decode vault of the letter.
 * Use encodeMessage to hide the letters content and use retrieveMessage to decode
 * into a human readable state.
 */
object LetterLock {

    fun retrieveMessage(message: String): String {
        return try {
            val decode = Base64.decode(message, Base64.NO_PADDING)
            String(decode)
        } catch (ex: IllegalArgumentException) {
            message
        }
    }

    fun encodeMessage(message: String): String {
        return Base64.encodeToString(message.toByteArray(), Base64.NO_PADDING)
    }
}
