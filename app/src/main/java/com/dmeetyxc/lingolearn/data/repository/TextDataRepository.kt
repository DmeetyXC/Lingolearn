package com.dmeetyxc.lingolearn.data.repository

import com.dmeetyxc.lingolearn.data.db.TextDataDao
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.TextData
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import com.dmeetyxc.lingolearn.ui.text.BookType
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextDataRepository @Inject constructor(
    private val database: TextDataDao,
    private val preferenceManager: PreferenceManager
) {

    suspend fun getTextDataBook(bookData: BookData): List<TextData> =
        database.getTextBook(bookData.bookName)

    fun insert(textData: List<TextData>) {
        database.insert(textData)
    }

    suspend fun loadFullTextData(bookData: BookData, typeLoadBook: BookType): String {
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

    private fun getFirebaseCollection() =
        Firebase.firestore.collection(BooksRepository.BASE_COLLECTION)
}