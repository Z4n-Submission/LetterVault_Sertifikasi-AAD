package com.google.developers.lettervault.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.developers.lettervault.data.DataRepository
import com.google.developers.lettervault.data.Letter
import com.google.developers.lettervault.util.Event

class LetterDetailViewModel(
    private val dataRepository: DataRepository,
    letterId: Long
) : ViewModel() {

    val letter: LiveData<Letter> = dataRepository.getLetter(letterId)
    private val _canOpen = MutableLiveData<Event<Letter>>()
    val canOpen: LiveData<Event<Letter>>
        get() = _canOpen

    fun tryOpening(letter: Letter) {
        _canOpen.value = Event(letter)
        open(letter)
    }

    /**
     * Update letter's date stamp when it cab be opened.
     */
    fun open(letter: Letter) {
        if (letter.expires < System.currentTimeMillis() && letter.opened == 0L) {
            dataRepository.openLetter(letter)
        }
    }

    fun delete() {
        letter.value?.let {
            dataRepository.delete(it)
        }
    }
}
