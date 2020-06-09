package com.dmitriybakunovich.languagelearning.data.repository

import androidx.lifecycle.LiveData
import com.dmitriybakunovich.languagelearning.data.db.DatabaseDao
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData

class TextDataRepository(private val databaseDao: DatabaseDao) {
    val allTextData: LiveData<List<TextData>> = databaseDao.getAllTextData()

    suspend fun insert(textData: TextData) {
        databaseDao.insert(textData)
    }
}