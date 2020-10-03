package com.dmitriybakunovich.languagelearning.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dmitriybakunovich.languagelearning.data.entity.BookData
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository

class FavoriteViewModel(repository: TextDataRepository) : ViewModel() {
    val favoriteBook: LiveData<List<BookData>> = repository.favoriteBook
}