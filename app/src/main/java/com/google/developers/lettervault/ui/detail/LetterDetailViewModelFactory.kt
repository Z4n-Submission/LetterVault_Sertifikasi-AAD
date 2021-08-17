package com.google.developers.lettervault.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.developers.lettervault.data.DataRepository

class LetterDetailViewModelFactory(val context: Context, val id: Long) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(DataRepository::class.java, id.javaClass)
            .newInstance(DataRepository.getInstance(context), id)
    }
}
