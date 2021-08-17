package com.google.developers.lettervault.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.google.developers.lettervault.data.DataRepository
import com.google.developers.lettervault.data.Letter
import com.google.developers.lettervault.data.LetterState
import java.util.*

/**
 * ViewModel for home activity
 */
class LetterViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val filter = MutableLiveData<LetterState>()
    val letters: LiveData<PagedList<Letter>> = Transformations.switchMap(filter) { input ->
        dataRepository.getLetters(input)
    }

    init {
        // Set default state value.
        filter.value = LetterState.ALL
    }

    /**
     * Update filter to a different state.
     *
     * @param itemName resource entry name identifier
     * @see R.menu.menu_list
     */
    fun filter(itemName: String) {
        val letterState = LetterState.valueOf(itemName.toUpperCase(Locale.US))
        filter.value = letterState
    }
}
