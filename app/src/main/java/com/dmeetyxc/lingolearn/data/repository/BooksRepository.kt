package com.dmeetyxc.lingolearn.data.repository

import com.dmeetyxc.lingolearn.data.db.DatabaseDao
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.TextData
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import com.dmeetyxc.lingolearn.ui.text.BookType
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BooksRepository @Inject constructor(
    private val database: DatabaseDao,
    private val preferenceManager: PreferenceManager,
    // Needed to correctly update selected books when exiting screen
    private val scope: CoroutineScope
) {

    private companion object {
        private const val BASE_COLLECTION = "books"
    }

    fun getAllBookData() = database.getAllBookData()

    suspend fun getBook(bookData: BookData): List<TextData> =
        database.getTextBook(bookData.bookName)

    suspend fun addNewBooks(childLanguage: String) {
        database.insertBooks(loadBooks(childLanguage))
    }

    fun insert(textData: List<TextData>) {
        database.insert(textData)
    }

    suspend fun deleteAllBooks() {
        database.deleteAllBook()
        database.deleteAllText()
    }

    fun getBooksNameLocal(): List<String> = database.getBooksName()

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

    suspend fun loadFullTextBook(bookData: BookData, typeLoadBook: BookType): String {
        return suspendCoroutine { cont ->
            getFirebaseCollection()
                .document(bookData.bookName)
                .get()
                .addOnSuccessListener { document ->
                    val typeLanguage = getTypeLanguage(typeLoadBook)
                    val textBook = document.getString(typeLanguage)
                    textBook?.let {
                        cont.resume(it)
                    }
                }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }

    private fun getTypeLanguage(typeLoadBook: BookType): String {
        var typeLanguage: String? = null
        if (typeLoadBook == BookType.MAIN) {
            typeLanguage = preferenceManager.getMainLanguage()
        } else if (typeLoadBook == BookType.CHILD) {
            typeLanguage = preferenceManager.getChildLanguage()
        }
        typeLanguage?.let {
            return typeLanguage
        }
        return ""
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