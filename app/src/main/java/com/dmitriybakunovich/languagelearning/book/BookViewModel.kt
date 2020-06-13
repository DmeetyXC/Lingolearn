package com.dmitriybakunovich.languagelearning.book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dmitriybakunovich.languagelearning.data.db.AppDatabase
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TextDataRepository
    val allBook: LiveData<List<BookData>>

    init {
        val databaseDao = AppDatabase
            .getDatabase(application, viewModelScope)
            .databaseDao()
        repository =
            TextDataRepository(
                databaseDao
            )
        allBook = repository.allBook
    }
}