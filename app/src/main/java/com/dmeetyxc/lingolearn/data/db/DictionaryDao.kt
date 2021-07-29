package com.dmeetyxc.lingolearn.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dmeetyxc.lingolearn.data.entity.Dictionary

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM dictionary")
    fun getAllDictionary(): LiveData<List<Dictionary>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dictionary: Dictionary)
}