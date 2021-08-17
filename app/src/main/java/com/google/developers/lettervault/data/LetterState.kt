package com.google.developers.lettervault.data

/**
 * A state to filter letters by.
 */
enum class LetterState {

    /**
     * All letters that has been opened. Has an {@link Letter#opened} timestamp.
     */
    OPENED,

    /**
     * Show all letters that has not expired and have not been opened/
     */
    FUTURE,

    /**
     * Show all letters.
     */
    ALL
}
