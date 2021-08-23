package com.google.developers.lettervault.ui.setting

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.developers.lettervault.R
import com.google.developers.lettervault.notification.NotificationWorker

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

            val setTheme = findPreference<ListPreference>(getString(R.string.pref_key_night))
        setTheme?.setOnPreferenceChangeListener { _, newValue ->
            when (newValue){
                getString(R.string.pref_night_auto) -> updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                getString(R.string.pref_night_on) -> updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                getString(R.string.pref_night_off) -> updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }

    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}
