package com.dmitriybakunovich.languagelearning.data.repository

import androidx.lifecycle.LiveData
import com.dmitriybakunovich.languagelearning.data.db.DatabaseDao
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.Dictionary
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData
import com.dmitriybakunovich.languagelearning.data.manager.ResourceManager
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextDataRepository(
    private val databaseDao: DatabaseDao,
    private val resourceManager: ResourceManager
) {
    //    val allBookWithText: LiveData<List<BookWithText>> = databaseDao.getBookWithText()
    val allBook: LiveData<List<BookData>> = databaseDao.getAllBookData()
    val dictionary: LiveData<List<Dictionary>> = databaseDao.getAllDictionary()

    suspend fun getBook(bookData: BookData): List<TextData> =
        databaseDao.getTextBook(bookData.bookName)

    fun getBookList(): List<BookData> = databaseDao.getAllBookDataList()

    fun insert(textData: List<TextData>) {
        databaseDao.insert(textData)
    }

    fun insert(dictionary: Dictionary) {
        databaseDao.insert(dictionary)
    }

    suspend fun addNewBooks() {
        databaseDao.insertBooks(loadBooksCloud())
    }

    suspend fun update(bookData: BookData) {
        databaseDao.update(bookData)
    }

    /**
     * Available display size in pixels for determine the number of characters
     */
    fun getMaxCountCharacters(): Int = resourceManager.getDisplayPixels()

    /**
     * Value for moving navigate focus text after touch display
     */
    fun getMovingNavigateValue(): Int = resourceManager.getMovingPixels()

    suspend fun loadFullTextBook(bookName: String, typeLoadBook: String): String {
        return suspendCoroutine { cont ->
            loadAllDataCloud()
                .addOnSuccessListener {
                    for (document in it) {
                        val textBook = searchTextBook(document, bookName, typeLoadBook)
                        if (textBook.isNotEmpty()) cont.resume(textBook)
                    }
                }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }

    private fun searchTextBook(
        document: QueryDocumentSnapshot,
        bookName: String,
        typeLoadBook: String
    ): String {
        if (bookName == document.id) {
            for (typeBook in document.data) {
                if (typeLoadBook == typeBook.key) {
                    return typeBook.value.toString()
                }
            }
        }
        return ""
    }

    // Get books name
    private suspend fun loadBooksCloud(): List<BookData> {
        val bookData = mutableListOf<BookData>()
        return suspendCoroutine { cont ->
            loadAllDataCloud().addOnSuccessListener {
                for (document in it) {
                    bookData.add(BookData(document.id, 0, false))
                }
                cont.resume(bookData)
            }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }

    private fun loadAllDataCloud(): Task<QuerySnapshot> {
        return Firebase.firestore.collection("books").get()
    }
}