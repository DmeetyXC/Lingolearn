package com.dmeetyxc.lingolearn.data.repository

import com.dmeetyxc.lingolearn.data.db.DictionaryDao
import com.dmeetyxc.lingolearn.data.entity.Dictionary
import javax.inject.Inject

class DictionaryRepository @Inject constructor(private val database: DictionaryDao) {

    fun getDictionary() = database.getAllDictionary()

    fun insert(dictionary: Dictionary) {
        database.insert(dictionary)
    }
}