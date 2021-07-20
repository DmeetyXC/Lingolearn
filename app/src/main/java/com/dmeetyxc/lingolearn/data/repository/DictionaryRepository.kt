package com.dmeetyxc.lingolearn.data.repository

import com.dmeetyxc.lingolearn.data.db.DatabaseDao
import com.dmeetyxc.lingolearn.data.entity.Dictionary
import javax.inject.Inject

class DictionaryRepository @Inject constructor(private val database: DatabaseDao) {

    fun getDictionary() = database.getAllDictionary()

    fun insert(dictionary: Dictionary) {
        database.insert(dictionary)
    }
}