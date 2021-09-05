package com.dmeetyxc.lingolearn.domain.dictionary

import androidx.lifecycle.LiveData
import com.dmeetyxc.lingolearn.data.entity.Dictionary

interface DictionaryRepository {

    fun getDictionary(): LiveData<List<Dictionary>>

    fun insert(dictionary: Dictionary)
}