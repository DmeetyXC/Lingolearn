package com.dmitriybakunovich.languagelearning.data.repository

import androidx.lifecycle.LiveData
import com.dmitriybakunovich.languagelearning.BuildConfig
import com.dmitriybakunovich.languagelearning.data.db.DatabaseDao
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.Dictionary
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData
import com.dmitriybakunovich.languagelearning.data.manager.PreferenceManager
import com.dmitriybakunovich.languagelearning.data.manager.ResourceManager
import com.dmitriybakunovich.languagelearning.data.network.ApiTranslate
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextDataRepository(
    private val databaseDao: DatabaseDao,
    private val resourceManager: ResourceManager,
    private val preferenceManager: PreferenceManager
) {
    //    val allBookWithText: LiveData<List<BookWithText>> = databaseDao.getBookWithText()
    val allBook: LiveData<List<BookData>> = databaseDao.getAllBookData()
    val dictionary: LiveData<List<Dictionary>> = databaseDao.getAllDictionary()

    companion object {
        private const val TRANSLATE_URL = "https://translate.yandex.net"
    }

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

    fun saveSelectLanguage(mainLanguage: String, childLanguage: String) {
        preferenceManager.saveLanguage(mainLanguage, childLanguage)
    }

    suspend fun translateText(textTranslate: String): String {
        val translateResult =
            getRetrofit().getTranslateText(
                BuildConfig.apiTranslateText,
                textTranslate,
                getLanguages()
            )
        return translateResult.text[0]
    }

    private fun getRetrofit(): ApiTranslate = Retrofit.Builder()
        .baseUrl(TRANSLATE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiTranslate::class.java)

    private fun getLanguages(): String {
        val main = preferenceManager.loadMainLanguage()
        val child = preferenceManager.loadChildLanguage()
        return "$main-$child"
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