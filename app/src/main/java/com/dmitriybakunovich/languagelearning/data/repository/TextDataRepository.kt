package com.dmitriybakunovich.languagelearning.data.repository

import androidx.lifecycle.LiveData
import com.dmitriybakunovich.languagelearning.data.db.DatabaseDao
import com.dmitriybakunovich.languagelearning.data.db.entity.BookWithText
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData

class TextDataRepository(private val databaseDao: DatabaseDao) {
    val allBookWithText: LiveData<List<BookWithText>> = databaseDao.getBookWithText()

    suspend fun insert(textData: TextData) {
        databaseDao.insert(textData)
    }
}