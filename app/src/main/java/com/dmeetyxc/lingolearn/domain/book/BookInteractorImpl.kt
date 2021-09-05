package com.dmeetyxc.lingolearn.domain.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.BookParentModel
import com.dmeetyxc.lingolearn.data.entity.TextData
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import com.dmeetyxc.lingolearn.data.manager.ResourceManager
import com.dmeetyxc.lingolearn.domain.text.TextRepository
import com.dmeetyxc.lingolearn.ui.text.BookType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookInteractorImpl @Inject constructor(
    private val bookRepository: BookRepository,
    private val textRepository: TextRepository,
    private val preferenceManager: PreferenceManager,
    private val resourceManager: ResourceManager
) : BookInteractor {

    override suspend fun checkNewBooks() {
        val booksNameLocal = bookRepository.getBooksNameLocal()
        if (booksNameLocal.isEmpty() || bookRepository.loadBooksNameCloud() != booksNameLocal) {
            val childLanguage = preferenceManager.getChildLanguage()
            if (!childLanguage.isNullOrEmpty()) {
                bookRepository.addNewBooks(childLanguage)
            }
        }
    }

    override fun getCategoryBook(): LiveData<List<BookParentModel>> =
        Transformations.map(bookRepository.getAllBookData()) {
            loadBookCategory(it)
        }

    override suspend fun loadBook(bookData: BookData) {
        withContext(Dispatchers.IO) {
            val textMain = async {
                textRepository.loadFullTextData(bookData, BookType.MAIN)
            }
            val textChild = async {
                textRepository.loadFullTextData(bookData, BookType.CHILD)
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

    override fun checkSaveLanguage(): Boolean {
        val mainLanguage = preferenceManager.getMainLanguage()
        return mainLanguage != null && mainLanguage.isNotEmpty()
    }

    override suspend fun updateFavoriteBook(book: BookData) {
        withContext(Dispatchers.IO) {
            if (book.isFavourite) {
                bookRepository.updateBook(book.copy(isFavourite = false))
            } else {
                bookRepository.updateBook(book.copy(isLoad = true, isFavourite = true))
            }
        }
    }

    /**
     * Get and convert books for each category,
     * updated after each change of books to the database
     */
    private fun loadBookCategory(allBookLoad: List<BookData>): List<BookParentModel> {
        if (allBookLoad.isEmpty()) return emptyList()
        return allBookLoad
            .distinctBy { it.bookCategory }
            .map { it.bookCategory }
            .map {
                BookParentModel(
                    it,
                    allBookLoad.filter { bookData -> bookData.bookCategory == it })
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
        textRepository.insert(textData)
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
        val sizeMax = resourceManager.getDisplayPixels()
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