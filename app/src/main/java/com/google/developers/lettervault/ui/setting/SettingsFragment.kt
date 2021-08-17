package com.google.developers.lettervault.ui.setting

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.developers.lettervault.R
import com.google.developers.lettervault.notification.NotificationWorker
import com.google.developers.lettervault.ui.detail.LetterDetailActivity.Companion.dataInput
import com.google.developers.lettervault.ui.detail.LetterDetailActivity.Companion.expireTime
import java.util.concurrent.TimeUnit

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        prefNotification?.setOnPreferenceChangeListener { _, newValue ->
            val booleanValue = newValue as Boolean
            val workManager = WorkManager.getInstance(context as Context)
            val switchReminder = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                .setInitialDelay(expireTime, TimeUnit.MINUTES)
                .setInputData(dataInput)
                .build()
            if (booleanValue){
                workManager.enqueue(switchReminder)
            } else {
                workManager.cancelWorkById(switchReminder.id)
            }
            true
        }

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
