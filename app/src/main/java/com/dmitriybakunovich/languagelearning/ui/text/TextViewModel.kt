package com.dmitriybakunovich.languagelearning.ui.text

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmitriybakunovich.languagelearning.data.entity.BookData
import com.dmitriybakunovich.languagelearning.data.entity.Dictionary
import com.dmitriybakunovich.languagelearning.data.entity.TextData
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository
import com.dmitriybakunovich.languagelearning.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TextViewModel(private val bookData: BookData, private val repository: TextDataRepository) :
    ViewModel() {
    // Required to receive a dedicated offer for further translation
    // To select text you only need textLineSelected
    private val textSelectedMain = MutableLiveData<SpannableString>()
    private val textSelectedChild = MutableLiveData<SpannableString>()
    val textLineSelected = MutableLiveData<Int>()
    val scrollTextState = MutableLiveData<Int>()
    val dictionaryModeState = MutableLiveData<Boolean>()
    val bookPage = MutableLiveData<TextData>()
    val lastPageState = MutableLiveData<Boolean>()
    val errorState = SingleLiveEvent<String>()
    val textSizeState = MutableLiveData<Float>()

    private lateinit var books: List<TextData>
    private var pageCurrentRead: Int

    init {
        pageCurrentRead = bookData.currentPageRead
        viewModelScope.launch(Dispatchers.IO) {
            setPageCurrentRead()
        }
        textSizeState.postValue((repository.getTextSize())?.toFloat())
    }

    fun dictionaryModeState(status: Boolean) {
        dictionaryModeState.postValue(status)
    }

    fun touchText(offset: Int, text: String, touchType: BookType) {
        val firstElement = searchFirstElement(offset, text)
        val lastElement = searchLastElement(offset, text)
        if (lastElement == 0) {
            return
        }
        val spannableString = selectionString(
            SpannableString(text),
            firstElement, lastElement
        )
        when (touchType) {
            BookType.MAIN -> textSelectedMain.postValue(spannableString)
            BookType.CHILD -> textSelectedChild.postValue(spannableString)
        }
    }

    fun searchNumberLineText(indexClick: Int, text: String) {
        var number = 0
        for (i in indexClick - 1 downTo 1) {
            val symbol1 = text[i]
            if (symbol1 == '.' || symbol1 == '!' || symbol1 == '?' || symbol1 == '…') {
                number++
            }
        }
        textLineSelected.postValue(number)
    }

    fun handleLineSelected(text: String, numberLine: Int): SpannableString {
        val firstIndex = getFirstElement(numberLine, text)
        val lastIndex = getLastElement(firstIndex, text)
        return selectionString(SpannableString(text), firstIndex, lastIndex)
    }

    fun scrollTextPosition(lineTwain: Int, line: Int) {
        val navigateValue = repository.getMovingNavigateValue()
        if (lineTwain <= line + 2) {
            scrollTextState.postValue(lineTwain + navigateValue)
        } else if (lineTwain >= 0) {
            scrollTextState.postValue(lineTwain - navigateValue)
        }
    }

    fun textDictionarySearch(textAll: String, min: Int, max: Int) {
        val selectedText: CharSequence = textAll.subSequence(min, max)
        val text = selectedText.toString()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val translateText = repository.translateText(text)
                repository.insert(Dictionary(text, translateText))
            } catch (e: Exception) {
                errorState.postValue(e.localizedMessage)
            }
        }
    }

    fun nextPageClick() {
        if (books.size - 1 > pageCurrentRead) {
            pageCurrentRead++
            bookPage.postValue(books[pageCurrentRead])
            if (pageCurrentRead + 1 == books.size) lastPageState.postValue(true)
            // Focus top page
            scrollTextState.postValue(-Int.MAX_VALUE)
        }
    }

    fun backPageClick() {
        // Checks equality to prevent double clicking
        if (pageCurrentRead == books.size) pageCurrentRead--

        if (pageCurrentRead != 0) {
            pageCurrentRead--
            bookPage.postValue(books[pageCurrentRead])
        }

        if (pageCurrentRead + 1 != books.size) lastPageState.postValue(false)
        // Focus top page
        scrollTextState.postValue(-Int.MAX_VALUE)
    }

    fun finishReadBook() {
        // End read book, set max size value + 1 for current page
        pageCurrentRead = books.size
    }

    private suspend fun setPageCurrentRead() {
        books = repository.getBook(bookData)
        if (books.size == pageCurrentRead) {
            bookPage.postValue(books[pageCurrentRead - 1])
        } else {
            bookPage.postValue(books[pageCurrentRead])
        }

        if (pageCurrentRead + 1 >= books.size) lastPageState.postValue(true)
    }

    private fun searchFirstElement(indexClick: Int, text: String): Int {
        for (i in indexClick - 1 downTo 1) {
            val symbol1 = text[i]
            if (symbol1 == '.' || symbol1 == '!' || symbol1 == '?' || symbol1 == '…') {
                return i + 2
            }
        }
        return 0
    }

    private fun searchLastElement(indexClick: Int, text: String): Int {
        for (i in indexClick + 1 until text.length) {
            val symbol2 = text[i]
            if (symbol2 == '.' || symbol2 == '!' || symbol2 == '?' || symbol2 == '…') {
                return i + 1
            }
        }
        return 0
    }

    private fun selectionString(
        spannableString: SpannableString, firstIndex: Int, lastIndex: Int
    ): SpannableString {
//        BackgroundColorSpan(Color.GREEN)
        spannableString.setSpan(
            ForegroundColorSpan(repository.getColorText()), firstIndex,
            lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    private fun getFirstElement(numberLine: Int, text: String): Int {
        var counter = 0
        for (i in text.indices) {
            val symbol = text[i]
            if (symbol == '.' || symbol == '!' || symbol == '?' || symbol == '…') {
                counter++
            }
            if (counter == numberLine && counter == 0) {
                return i
            } else if (counter == numberLine) {
                return i + 1
            }
        }
        return counter
    }

    private fun getLastElement(firstIndex: Int, text: String): Int {
        var lastIndex = firstIndex
        for (i in firstIndex + 1 until text.length) {
            lastIndex++
            val symbol = text[i]
            if (symbol == '.' || symbol == '!' || symbol == '?' || symbol == '…') {
                return lastIndex + 1
            }
        }
        return lastIndex + 1
    }

    override fun onCleared() {
        GlobalScope.launch(Dispatchers.IO) {
            repository.update(
                bookData.copy(
                    currentPageRead = pageCurrentRead, isLoad = true,
                    numberPages = books.size
                )
            )
        }
        super.onCleared()
    }
}
