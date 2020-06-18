package com.dmitriybakunovich.languagelearning.book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dmitriybakunovich.languagelearning.data.db.AppDatabase
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    fun initParserBook(bookData: BookData) {
        val fullTextMainBook = repository.loadFullTextMainBook(bookData.bookName)
        // TODO fullTextChildBook need translate
        val fullTextChildBook = repository.loadFullTextChildBook(bookData.bookName)
        val parseMainBook = parseBook(fullTextMainBook)
        val parseChildBook = parseBook(fullTextChildBook)
        saveTextBook(parseMainBook, parseChildBook, bookData)
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
    ) =
        runBlocking(Dispatchers.IO) {
            val job = launch(Dispatchers.IO) {
                fillTextData(parseTextMain, parseTextChild, bookData)
            }
            job.join()
        }

    private suspend fun fillTextData(
        parseTextMain: List<String>,
        parseTextChild: List<String>,
        bookData: BookData
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in parseTextMain.indices) {
                val textMain = parseTextMain[i]
                val textChild = parseTextChild[i]
                repository.insert(TextData(bookData.bookName, textMain, textChild))
            }
        }
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