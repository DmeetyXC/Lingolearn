package com.dmeetyxc.lingolearn.ui.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.BookParentModel
import com.dmeetyxc.lingolearn.data.manager.ConnectionManager
import com.dmeetyxc.lingolearn.domain.book.BookInteractor
import com.dmeetyxc.lingolearn.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookInteractor: BookInteractor,
    private val connectionManager: ConnectionManager
) : ViewModel() {

    val progressState = MutableLiveData<Boolean>()
    val initBookState = SingleLiveEvent<BookData>()
    val languageState = SingleLiveEvent<Boolean>()

    init {
        languageState.postValue(bookInteractor.checkSaveLanguage())
        checkNewBooks()
    }

    fun checkNewBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            progressState.postValue(true)
            bookInteractor.checkNewBooks()
            progressState.postValue(false)
        }
    }

    fun booksCategoryState(): LiveData<List<BookParentModel>> =
        bookInteractor.getCategoryBook()

    fun networkState() = connectionManager.getNetworkStatus()

    fun handleItemClick(book: BookData) {
        viewModelScope.launch {
            if (!book.isLoad) loadBook(book)
            initBookState.postValue(book)
        }
    }

    fun addFavoriteBook(book: BookData) {
        viewModelScope.launch {
            if (!book.isLoad) loadBook(book)
            bookInteractor.updateFavoriteBook(book)
        }
    }

    private suspend fun loadBook(book: BookData) {
        progressState.postValue(true)
        bookInteractor.loadBook(book)
        progressState.postValue(false)
    }
}