package com.dmeetyxc.lingolearn.ui.text

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.TextData
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager.Companion.BOOK
import com.dmeetyxc.lingolearn.domain.interactor.TextInteractor
import com.dmeetyxc.lingolearn.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextViewModel @Inject constructor(
    private val textInteractor: TextInteractor,
    state: SavedStateHandle
) : ViewModel() {

    val textLineSelected = MutableLiveData<Int>()
    val scrollTextState = MutableLiveData<Int>()
    val dictionaryModeState = MutableLiveData<Boolean>()
    val bookPage = MutableLiveData<TextData>()
    val lastPageState = MutableLiveData<Boolean>()
    val errorState = SingleLiveEvent<String>()
    val textSizeState = MutableLiveData<Float>()

    private val bookData = state.get<BookData>(BOOK)!!
    private lateinit var books: List<TextData>
    private var pageCurrentRead: Int

    init {
        pageCurrentRead = bookData.currentPageRead
        viewModelScope.launch(Dispatchers.IO) {
            setPageCurrentRead()
        }
        textSizeState.postValue(textInteractor.getTextSize())
    }

    fun dictionaryModeState(status: Boolean) {
        dictionaryModeState.postValue(status)
    }

    fun handleLineSelected(text: String, numberLine: Int) =
        textInteractor.lineSelectedText(text, numberLine)

    fun searchNumberLineText(indexClick: Int, text: String) {
        textLineSelected.postValue(textInteractor.searchNumberLineText(indexClick, text))
    }

    fun scrollTextPosition(lineTwain: Int, line: Int) {
        val navigateValue = textInteractor.getMovingTextPixels()
        if (lineTwain <= line + 2) {
            scrollTextState.postValue(lineTwain + navigateValue)
        } else if (lineTwain >= 0) {
            scrollTextState.postValue(lineTwain - navigateValue)
        }
    }

    fun textDictionarySearch(textAll: String, min: Int, max: Int) {
        val selectedText = textAll.subSequence(min, max)
        viewModelScope.launch(Dispatchers.IO) {
            textInteractor.translateText(selectedText.toString()) {
                if (it is TextInteractor.TextTranslateResponse.Error)
                    errorState.postValue(it.errorMessage.localizedMessage)
            }
        }
    }

    fun nextPageClick() {
        if (books.size - 1 > pageCurrentRead) {
            pageCurrentRead++
            bookPage.postValue(books[pageCurrentRead])
            changeScrollState()
        }
    }

    fun backPageClick() {
        // Checks equality to prevent double clicking
        if (pageCurrentRead == books.size) pageCurrentRead--

        if (pageCurrentRead != 0) {
            pageCurrentRead--
            bookPage.postValue(books[pageCurrentRead])
        }
        changeScrollState()
    }

    fun finishReadBook() {
        // End read book, set max size value + 1 for current page
        pageCurrentRead = books.size
    }

    private suspend fun setPageCurrentRead() {
        books = textInteractor.getTextData(bookData)
        if (books.size == pageCurrentRead) {
            bookPage.postValue(books[pageCurrentRead - 1])
        } else {
            bookPage.postValue(books[pageCurrentRead])
        }

        if (pageCurrentRead + 1 >= books.size) lastPageState.postValue(true)
    }

    private fun changeScrollState() {
        lastPageState.postValue(pageCurrentRead + 1 == books.size)
        // Focus top page
        scrollTextState.postValue(-Int.MAX_VALUE)
    }

    override fun onCleared() {
        textInteractor.updateBook(
            bookData.copy(
                currentPageRead = pageCurrentRead, isLoad = true, numberPages = books.size
            )
        )
        super.onCleared()
    }
}
