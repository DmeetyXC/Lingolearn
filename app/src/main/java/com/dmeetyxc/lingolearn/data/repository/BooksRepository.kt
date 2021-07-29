package com.dmeetyxc.lingolearn.data.repository

import com.dmeetyxc.lingolearn.data.db.BookDao
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BooksRepository @Inject constructor(
    private val database: BookDao,
    // Needed to correctly update selected books when exiting screen
    private val scope: CoroutineScope
) {

    companion object {
        const val BASE_COLLECTION = "books"
    }

    fun getAllBookData() = database.getAllBookData()

    suspend fun addNewBooks(childLanguage: String) {
        database.insertBooks(loadBooks(childLanguage))
    }

    suspend fun deleteAllBooks() {
        database.deleteBooks()
    }

    fun getBooksNameLocal(): List<String> = database.getBooksName()

    fun getFavoriteBook() = database.getFavoriteBook()

    fun updateBook(bookData: BookData) {
        scope.launch {
            database.update(bookData)
        }
    }

    suspend fun loadBooksNameCloud(): List<String> {
        val bookData = mutableListOf<String>()
        return suspendCoroutine { cont ->
            getFirebaseCollection()
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        bookData.add(document.id)
                    }
                    cont.resume(bookData)
                }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }

    private suspend fun loadBooks(childLanguage: String): List<BookData> {
        val bookData = mutableListOf<BookData>()
        return suspendCoroutine { cont ->
            getFirebaseCollection()
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        bookData.add(
                            BookData(
                                document.id, document.getString("book_$childLanguage")!!,
                                document.getString("category_$childLanguage")!!, 0,
                                false, document.getString("image")
                            )
                        )
                    }
                    cont.resume(bookData)
                }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }

    private fun getFirebaseCollection() = Firebase.firestore.collection(BASE_COLLECTION)
}