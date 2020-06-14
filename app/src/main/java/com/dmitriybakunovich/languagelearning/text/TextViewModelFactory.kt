package com.dmitriybakunovich.languagelearning.text

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData

@Suppress("UNCHECKED_CAST")
class TextViewModelFactory(private val application: Application, private val bookData: BookData?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        bookData?.let { TextViewModel(application = application, bookData = it) } as T
}