package com.google.developers.lettervault.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.developers.lettervault.data.DataRepository
import com.google.developers.lettervault.data.Letter

/**
 * ViewMode for the HomeActivity only holds recent letter.
 */
class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private lateinit var recentLetter : LiveData<Letter>

    fun setRecentLetter(){
        recentLetter = dataRepository.getRecentLetter()
    }

    fun getRecentLetter() : LiveData<Letter> {
        return recentLetter
    }
}
