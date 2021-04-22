package com.dmeetyxc.lingolearn.ui.book

import androidx.lifecycle.*
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.BookParentModel
import com.dmeetyxc.lingolearn.data.entity.TextData
import com.dmeetyxc.lingolearn.data.manager.ConnectionManager
import com.dmeetyxc.lingolearn.data.repository.TextDataRepository
import com.dmeetyxc.lingolearn.ui.text.BookType
import com.dmeetyxc.lingolearn.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookViewModel(
    private val repository: TextDataRepository,
    val networkConnectionState: ConnectionManager,
) : ViewModel() {

    val progressState = MutableLiveData<Boolean>()
    val initBookState = SingleLiveEvent<BookData>()
    val languageState = SingleLiveEvent<Boolean>()

    init {
        languageState.postValue(checkSaveLanguage())
        checkNewBooks()
    }

    fun checkNewBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            val booksNameCloud = repository.loadBooksNameCloud()
            if (booksNameCloud.isEmpty() || booksNameCloud != repository.getBooksNameLocal()) {
                progressState.postValue(true)
                val childLanguage = repository.getChildLanguage()
                if (!childLanguage.isNullOrEmpty()) {
                    repository.addNewBooks(childLanguage)
                }
            }
            progressState.postValue(false)
        }
    }

    fun booksCategoryState(): LiveData<List<BookParentModel>> =
        Transformations.map(repository.allBook) {
            loadBookCategory(it)
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

    private fun checkSaveLanguage(): Boolean {
        val mainLanguage = repository.getMainLanguage()
        return mainLanguage != null && mainLanguage.isNotEmpty()
    }

    /**
     * Get and convert books for each category,
     * updated after each change of books to the database
     */
    private fun loadBookCategory(allBookLoad: List<BookData>): List<BookParentModel> {
        if (allBookLoad.isEmpty()) {
            checkNewBooks()
            return emptyList()
        }
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
            val parseText = parseText(
                parseMainBook.await(),
                parseChildBook.await()
            )
            saveTextBook(parseText.first, parseText.second, bookData)
        }
    }

    private fun parseBook(fullTextBook: String): MutableList<String> {
        val splitText = mutableListOf<String>()
        var i = 0
        while (i < fullTextBook.length) {
            val firstElement = searchFirstElement(i, fullTextBook)
            val lastElement = searchLastElement(i, fullTextBook)
            // Space need to separate lines
            splitText.add(fullTextBook.substring(firstElement, lastElement).trim() + " ")
            i = lastElement + 1
        }
        return splitText
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

    /**
     * Parses text no more than the maximum specified number, the text is divided by sentences.
     * The child text depends on the main text
     * @return prepared pair lists consisting of main and child list to save in database
     */
    private fun parseText(arrMain: MutableList<String>, arrChild: MutableList<String>):
            Pair<List<String>, List<String>> {
        val finalArrMain = mutableListOf<String>()
        val finalArrChild = mutableListOf<String>()
        val stringMain: StringBuilder = StringBuilder()
        val stringChild: StringBuilder = StringBuilder()
        val sizeMax = repository.getMaxCountCharacters()
        var lengthText = 0
        for (i in arrMain.indices) {
            lengthText += arrMain[i].length
            if (lengthText < sizeMax) {
                stringMain.append(arrMain[i])
                stringChild.append(arrChild[i])
            } else {
                if (stringMain.isEmpty()) {
                    stringMain.append(arrMain[i])
                    stringChild.append(arrChild[i])
                } else {
                    finalArrMain.add(stringMain.toString())
                    finalArrChild.add(stringChild.toString())
                    stringMain.clear().append(arrMain[i])
                    stringChild.clear().append(arrChild[i])
                    lengthText = 0
                }
            }
        }
        finalArrMain.add(stringMain.toString())
        finalArrChild.add(stringChild.toString())
        return Pair(finalArrMain, finalArrChild)
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
}