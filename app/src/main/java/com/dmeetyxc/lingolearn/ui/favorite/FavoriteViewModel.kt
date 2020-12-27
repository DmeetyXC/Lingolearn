package com.dmeetyxc.lingolearn.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.repository.TextDataRepository

class FavoriteViewModel(repository: TextDataRepository) : ViewModel() {
    val favoriteBook: LiveData<List<BookData>> = repository.favoriteBook
}