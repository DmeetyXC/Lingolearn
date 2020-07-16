package com.dmitriybakunovich.languagelearning.ui.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository
import com.dmitriybakunovich.languagelearning.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookViewModel(private val repository: TextDataRepository) :
    ViewModel() {
    val progressState = MutableLiveData<Boolean>()
    val initBookState = SingleLiveEvent<BookData>()
    val allBook: LiveData<List<BookData>> = repository.allBook

    init {
        viewModelScope.launch(Dispatchers.IO) { checkNewBooks() }
    }

    fun handleItemClick(book: BookData) {
        viewModelScope.launch {
            if (!book.isLoad) {
                progressState.postValue(true)
                initBook(book)
                progressState.postValue(false)
            }
            initBookState.postValue(book)
        }
    }

    fun addFavoriteBook(book: BookData) {
        viewModelScope.launch(Dispatchers.IO) {
            if (book.isFavourite) {
                repository.update(
                    BookData(
                        book.bookName, book.currentPageRead, book.isLoad,
                        book.numberPages, false
                    )
                )
            } else {
                repository.update(
                    BookData(
                        book.bookName, book.currentPageRead,
                        book.isLoad, book.numberPages, true
                    )
                )
            }
        }
    }

    private suspend fun checkNewBooks() {
        val books = repository.getBookList()
        // TODO Make check time last add book
        if (books.isEmpty()) {
            progressState.postValue(true)
            repository.addNewBooks()
            progressState.postValue(false)
        }
    }

    private suspend fun initBook(bookData: BookData) {
        withContext(Dispatchers.IO) {
            val textMain = async {
                repository.loadFullTextBook(bookData.bookName, "bookMain")
            }
            val textChild = async {
                repository.loadFullTextBook(bookData.bookName, "bookChild")
            }

            val parseMainBook = async { parseBook(textMain.await()) }
            val parseChildBook = async { parseBook(textChild.await()) }

            saveTextBook(parseMainBook.await(), parseChildBook.await(), bookData)
        }
    }

    private fun parseBook(fullTextBook: String): List<String> {
        val splitText = mutableListOf<String>()
        var i = 0
        while (i < fullTextBook.length) {
            val firstElement = searchFirstElement(i, fullTextBook)
            val lastElement = searchLastElement(i, fullTextBook)
            // Space need to separate lines
            splitText.add(fullTextBook.substring(firstElement, lastElement) + " ")
            i = lastElement + 1
        }
        return parseText(splitText)
    }

    private fun saveTextBook(
        parseTextMain: List<String>,
        parseTextChild: List<String>,
        bookData: BookData
    ) {
        if (parseTextMain.isNotEmpty() && parseTextChild.isNotEmpty()) {
            fillTextData(parseTextMain, parseTextChild, bookData)
        }
    }

    private fun fillTextData(
        parseTextMain: List<String>,
        parseTextChild: List<String>,
        bookData: BookData
    ) {
        val textData = mutableListOf<TextData>()
        for (i in parseTextMain.indices) {
            val textMain = parseTextMain[i]
            val textChild = parseTextChild[i]
            textData.add(TextData(bookData.bookName, textMain, textChild))
        }
        repository.insert(textData)
    }

    private fun parseText(arr: MutableList<String>): List<String> {
        val finalArr = mutableListOf<String>()
        val string: StringBuilder = StringBuilder()
        val size = 1000
        var length = 0
        for (j in arr) {
            length += j.length
            if (length < size) {
                string.append(j)
            } else {
                if (string.isEmpty()) {
                    string.append(j)
                } else {
                    finalArr.add(string.toString())
                    string.clear().append(j)
                    length = 0
                }
            }
        }
        finalArr.add(string.toString())
        return finalArr
    }

    // TODO need move this to interactor(this code is also in textViewModel)
    private fun searchFirstElement(indexClick: Int, text: String): Int {
        for (i in indexClick - 1 downTo 1) {
            val symbol1 = text[i]
            if (symbol1 == '.' || symbol1 == '!' || symbol1 == '?') {
                return i + 2
            }
        }
        return 0
    }

    private fun searchLastElement(indexClick: Int, text: String): Int {
        for (i in indexClick + 1 until text.length) {
            val symbol2 = text[i]
            if (symbol2 == '.' || symbol2 == '!' || symbol2 == '?') {
                return i + 1
            }
        }
        return 0
    }
}