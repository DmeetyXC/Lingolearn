package com.dmitriybakunovich.languagelearning.book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dmitriybakunovich.languagelearning.data.db.AppDatabase
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TextDataRepository
    val allBook: LiveData<List<BookData>>
    val progressState = MutableLiveData<Pair<Boolean, BookData>>()

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

    fun initBook(bookData: BookData) {
        progressState.postValue(Pair(true, bookData))
        viewModelScope.launch(Dispatchers.IO) {
            val textMain = async {
                repository.loadFullTextBook(bookData.bookName, "bookMain")
            }
            val textChild = async {
                repository.loadFullTextBook(bookData.bookName, "bookChild")
            }

            val parseMainBook = async { parseBook(textMain.await()) }
            val parseChildBook = async { parseBook(textChild.await()) }

            saveTextBook(parseMainBook.await(), parseChildBook.await(), bookData)
            progressState.postValue(Pair(false, bookData))
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

    private suspend fun saveTextBook(
        parseTextMain: List<String>,
        parseTextChild: List<String>,
        bookData: BookData
    ) {
        if (parseTextMain.isNotEmpty() && parseTextChild.isNotEmpty()) {
            fillTextData(parseTextMain, parseTextChild, bookData)
        }
    }

    private suspend fun fillTextData(
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