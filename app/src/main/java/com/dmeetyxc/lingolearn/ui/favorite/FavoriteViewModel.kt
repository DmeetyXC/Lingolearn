package com.dmeetyxc.lingolearn.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.repository.FavoriteBooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(repository: FavoriteBooksRepository) : ViewModel() {
    val favoriteBook: LiveData<List<BookData>> = repository.getFavoriteBook()
}