package com.dmitriybakunovich.languagelearning.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM text_data")
    fun getAllTextData(): LiveData<List<TextData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(textData: TextData)

    @Update
    suspend fun update(textData: TextData)

    @Delete
    suspend fun delete(textData: TextData)

    @Query("DELETE FROM text_data")
    suspend fun deleteAll()
}