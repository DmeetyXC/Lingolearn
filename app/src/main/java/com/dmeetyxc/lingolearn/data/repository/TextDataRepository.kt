package com.dmeetyxc.lingolearn.data.repository

import androidx.lifecycle.LiveData
import com.dmeetyxc.lingolearn.BuildConfig
import com.dmeetyxc.lingolearn.data.db.DatabaseDao
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.Dictionary
import com.dmeetyxc.lingolearn.data.entity.TextData
import com.dmeetyxc.lingolearn.data.entity.TranslationData
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import com.dmeetyxc.lingolearn.data.manager.ResourceManager
import com.dmeetyxc.lingolearn.data.network.ApiTranslate
import com.dmeetyxc.lingolearn.ui.text.BookType
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextDataRepository(
    private val databaseDao: DatabaseDao,
    private val preferenceManager: PreferenceManager,
    private val resourceManager: ResourceManager,
    private val scope: CoroutineScope
) {
    val allBook: LiveData<List<BookData>> = databaseDao.getAllBookData()
    val dictionary: LiveData<List<Dictionary>> = databaseDao.getAllDictionary()
    val favoriteBook: LiveData<List<BookData>> = databaseDao.getFavoriteBook()

    companion object {
        private const val TRANSLATE_URL = "https://api.cognitive.microsofttranslator.com"
        private const val BASE_COLLECTION = "books"
    }

    suspend fun getBook(bookData: BookData): List<TextData> =
        databaseDao.getTextBook(bookData.bookName)

    fun insert(textData: List<TextData>) {
        databaseDao.insert(textData)
    }

    fun insert(dictionary: Dictionary) {
        databaseDao.insert(dictionary)
    }

    suspend fun addNewBooks(childLanguage: String) {
        databaseDao.insertBooks(loadBooks(childLanguage))
    }

    fun update(bookData: BookData) {
        scope.launch {
            databaseDao.update(bookData)
        }
    }

    suspend fun deleteAllData() {
        databaseDao.deleteAllBook()
        databaseDao.deleteAllText()
    }

    fun getTextSize() = preferenceManager.getTextSize()?.toFloat()

    fun saveSelectLanguage(mainLanguage: String, childLanguage: String) {
        preferenceManager.saveLanguages(mainLanguage, childLanguage)
    }

    suspend fun translateText(textTranslate: String): String {
        val translateResult =
            getRetrofit().getTranslateText(
                BuildConfig.apiTranslateText,
                getMainLanguage()!!,
                getChildLanguage()!!,
                listOf(TranslationData(textTranslate))
            )
        return translateResult[0].translation[0].text
    }

    fun getMainLanguage() = preferenceManager.getLanguage(PreferenceManager.MAIN)

    fun getChildLanguage() = preferenceManager.getLanguage(PreferenceManager.CHILD)

    fun getColorText() = resourceManager.getColorSelectText()

    fun getBooksNameLocal(): List<String> = databaseDao.getBooksName()

    fun getAppTheme() = preferenceManager.getAppTheme()

    fun getLanguagesValue(): Array<String> = resourceManager.getLanguagesValue()

    fun getLanguages(): Array<String> = resourceManager.getLanguages()

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

    /**
     * Available display size in pixels for determine the number of characters
     */
    fun getMaxCountCharacters(): Int = resourceManager.getDisplayPixels()

    /**
     * Value for moving navigate focus text after touch display
     */
    fun getMovingNavigateValue(): Int = resourceManager.getMovingPixels()

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

    private fun getRetrofit(): ApiTranslate = Retrofit.Builder()
        .baseUrl(TRANSLATE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiTranslate::class.java)

    private fun getTypeLanguage(typeLoadBook: BookType): String {
        var typeLanguage: String? = null
        if (typeLoadBook == BookType.MAIN) {
            typeLanguage = preferenceManager.getLanguage(PreferenceManager.MAIN)
        } else if (typeLoadBook == BookType.CHILD) {
            typeLanguage = preferenceManager.getLanguage(PreferenceManager.CHILD)
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