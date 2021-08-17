package com.google.developers.lettervault.util

import androidx.appcompat.app.AppCompatDelegate

/**
 * This enum class holds values for changing qualifying resources to night mode
 * and used across all activities.
 */
enum class NightMode(val value: Int) {

    /**
     * Mode use to determined if its night or not and use applicable resources.
     *
     * @see AppCompatDelegate.MODE_NIGHT_AUTO
     */
    AUTO(AppCompatDelegate.MODE_NIGHT_AUTO),

    /**
     * Mode use to set always night mode use night resources.
     *
     * @see AppCompatDelegate.MODE_NIGHT_YES
     */
    ON(AppCompatDelegate.MODE_NIGHT_YES),

    /**
     * Mode use to not set night mode use default resources.
     *
     * @see AppCompatDelegate.MODE_NIGHT_NO
     */
    OFF(AppCompatDelegate.MODE_NIGHT_NO)
}
