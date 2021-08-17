package com.google.developers.lettervault.ui.add

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.developers.lettervault.data.DataRepository
import com.google.developers.lettervault.data.Letter
import com.google.developers.lettervault.util.Event
import com.google.developers.lettervault.util.LetterLock

class AddLetterViewModel(private val dataRepository: DataRepository) : ViewModel() {

    val created = System.currentTimeMillis()
    private var expires = System.currentTimeMillis()
    private val _saved = MutableLiveData<Event<Boolean>>()
    val saved: LiveData<Event<Boolean>>
        get() = _saved

    fun save(subject: String, content: String) {
        if (content.isEmpty()) {
            _saved.value = Event(false)
            return
        }

        val letter = Letter(
            subject = LetterLock.encodeMessage(subject),
            content = LetterLock.encodeMessage(content),
            created = created,
            expires = expires
        )
        dataRepository.save(letter)
        _saved.value = Event(true)
    }

    /**
     * Set expiration time when letter can be readable. If selected time is one minute is past
     * add one day to the expiration else keep the selected expiration time.
     */
    fun setExpirationTime(hourOfDay: Int, minute: Int) {
        val expirationTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val timeDifference = System.currentTimeMillis() - expirationTime.timeInMillis
        if (timeDifference >= -1L) {
            expirationTime.add(Calendar.DATE, 1)
        }

        expires = expirationTime.timeInMillis
    }
}
