package com.dmitriybakunovich.languagelearning.data.repository

import androidx.lifecycle.LiveData
import com.dmitriybakunovich.languagelearning.data.db.DatabaseDao
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextDataRepository(private val databaseDao: DatabaseDao) {
    //    val allBookWithText: LiveData<List<BookWithText>> = databaseDao.getBookWithText()
    val allBook: LiveData<List<BookData>> = databaseDao.getAllBookData()

    suspend fun getBook(bookData: BookData): List<TextData> =
        databaseDao.getTextBook(bookData.bookName)

    suspend fun getBookList(): List<BookData> = databaseDao.getAllBookDataList()

    suspend fun insert(textData: List<TextData>) {
        databaseDao.insert(textData)
    }

    suspend fun addNewBooks() {
        databaseDao.insertBooks(loadBooksCloud())
    }

    suspend fun update(bookData: BookData) {
        databaseDao.update(bookData)
    }

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