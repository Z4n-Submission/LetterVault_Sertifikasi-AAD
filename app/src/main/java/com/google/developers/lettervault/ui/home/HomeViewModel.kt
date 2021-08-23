package com.google.developers.lettervault.ui.home

import androidx.lifecycle.ViewModel
import com.google.developers.lettervault.data.DataRepository

/**
 * ViewMode for the HomeActivity only holds recent letter.
 */
class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {
    //panggil data repo untuk menampilkan recent
    //buat fun untuk menampilkan data pada home
}
