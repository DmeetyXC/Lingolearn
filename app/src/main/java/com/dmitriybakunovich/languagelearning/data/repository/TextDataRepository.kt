package com.dmitriybakunovich.languagelearning.data.repository

import androidx.lifecycle.LiveData
import com.dmitriybakunovich.languagelearning.data.db.DatabaseDao
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.BookWithText
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextDataRepository(private val databaseDao: DatabaseDao) {
    val allBookWithText: LiveData<List<BookWithText>> = databaseDao.getBookWithText()
    val allBook: LiveData<List<BookData>> = databaseDao.getAllBookData()
    val db = Firebase.firestore

    suspend fun getBook(bookData: BookData): List<TextData> =
        databaseDao.getTextBook(bookData.bookName)

    suspend fun insert(textData: TextData) {
        databaseDao.insert(textData)
    }

    suspend fun insert(textData: List<TextData>) {
        databaseDao.insert(textData)
    }

    suspend fun update(bookData: BookData) {
        databaseDao.update(bookData)
    }

    suspend fun update(textData: TextData) {
        databaseDao.update(textData)
    }

    suspend fun loadFullTextBook(bookName: String, typeLoadBook: String): String {
        return suspendCoroutine { cont ->
            loadAllDataCloud(bookName).addOnSuccessListener {
                for (document in it) {
                    for (typeBook in document.data) {
                        if (typeLoadBook == typeBook.key) {
                            val textLoad = typeBook.value.toString()
                            cont.resume(textLoad)
                        }
                    }
                }
            }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }

    private fun loadAllDataCloud(bookName: String): Task<QuerySnapshot> {
        return db.collection(bookName).get()
    }
}