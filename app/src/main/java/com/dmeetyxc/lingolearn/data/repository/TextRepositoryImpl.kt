package com.dmeetyxc.lingolearn.data.repository

import com.dmeetyxc.lingolearn.data.db.TextDataDao
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.TextData
import com.dmeetyxc.lingolearn.domain.settings.AppSettings
import com.dmeetyxc.lingolearn.domain.text.TextRepository
import com.dmeetyxc.lingolearn.ui.text.BookType
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextRepositoryImpl @Inject constructor(
    private val database: TextDataDao,
    private val appSettings: AppSettings
) : TextRepository {

    override suspend fun getTextDataBook(bookData: BookData): List<TextData> =
        database.getTextBook(bookData.bookName)

    override fun insert(textData: List<TextData>) {
        database.insert(textData)
    }

    override suspend fun loadFullTextData(bookData: BookData, typeLoadBook: BookType): String {
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
            typeLanguage = appSettings.getMainLanguage()
        } else if (typeLoadBook == BookType.CHILD) {
            typeLanguage = appSettings.getChildLanguage()
        }
        typeLanguage?.let {
            return typeLanguage
        }
        return ""
    }

    private fun getFirebaseCollection() =
        Firebase.firestore.collection(BookRepositoryImpl.BASE_COLLECTION)
}