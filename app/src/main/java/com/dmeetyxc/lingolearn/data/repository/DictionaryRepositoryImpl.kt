package com.dmeetyxc.lingolearn.data.repository

import com.dmeetyxc.lingolearn.data.db.DictionaryDao
import com.dmeetyxc.lingolearn.data.entity.Dictionary
import com.dmeetyxc.lingolearn.domain.dictionary.DictionaryRepository
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val database: DictionaryDao
) : DictionaryRepository {

    override fun getDictionary() = database.getAllDictionary()

    override fun insert(dictionary: Dictionary) {
        database.insert(dictionary)
    }
}