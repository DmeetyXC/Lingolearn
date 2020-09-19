package com.dmitriybakunovich.languagelearning.ui.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData
import com.dmitriybakunovich.languagelearning.data.model.BookParentModel
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository
import com.dmitriybakunovich.languagelearning.ui.text.BookType
import com.dmitriybakunovich.languagelearning.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookViewModel(private val repository: TextDataRepository) : ViewModel() {

    val progressState = MutableLiveData<Boolean>()
    val initBookState = SingleLiveEvent<BookData>()
    val allBookCategory = Transformations.map(repository.allBook) {
        return@map loadBookCategory(it)
    }

    init {
        checkNewBooks()
    }

    fun checkNewBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getBooksNameLocal() != repository.loadBooksNameCloud()) {
                progressState.postValue(true)
                repository.addNewBooks()
            }
            progressState.postValue(false)
        }
    }

    fun handleItemClick(book: BookData) {
        viewModelScope.launch {
            if (!book.isLoad) {
                loadBook(book)
            }
            initBookState.postValue(book)
        }
    }

    fun addFavoriteBook(book: BookData) {
        viewModelScope.launch {
            if (!book.isLoad) {
                loadBook(book)
            }
            updateFavoriteBook(book)
        }
    }

    fun checkSaveLanguage(): Boolean {
        val mainLanguage = repository.getMainLanguage()
        return mainLanguage != null && mainLanguage.isNotEmpty()
    }

    /**
     * Get and convert books for each category,
     * updated after each change of books to the database
     */
    private fun loadBookCategory(allBookLoad: List<BookData>): List<BookParentModel> {
        return allBookLoad
            .distinctBy { it.bookCategory }
            .map { it.bookCategory }
            .map {
                BookParentModel(
                    it,
                    allBookLoad.filter { bookData -> bookData.bookCategory == it })
            }
    }

    private suspend fun loadBook(book: BookData) {
        progressState.postValue(true)
        initBook(book)
        progressState.postValue(false)
    }

    private suspend fun updateFavoriteBook(book: BookData) {
        withContext(Dispatchers.IO) {
            if (book.isFavourite) {
                repository.update(book.copy(isFavourite = false))
            } else {
                repository.update(book.copy(isLoad = true, isFavourite = true))
            }
        }
    }

    private suspend fun initBook(bookData: BookData) {
        withContext(Dispatchers.IO) {
            val textMain = async {
                repository.loadFullTextBook(bookData, BookType.MAIN)
            }
            val textChild = async {
                repository.loadFullTextBook(bookData, BookType.CHILD)
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
            splitText.add(fullTextBook.substring(firstElement, lastElement).trim() + " ")
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
        val size = repository.getMaxCountCharacters()
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