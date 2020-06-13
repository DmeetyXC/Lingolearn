package com.dmitriybakunovich.languagelearning.text

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class TextViewModelFactory(private val application: Application, private val bookName: String?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        bookName?.let { TextViewModel(application = application, bookName = it) } as T
}