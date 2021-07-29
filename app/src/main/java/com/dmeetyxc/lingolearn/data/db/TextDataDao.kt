package com.dmeetyxc.lingolearn.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dmeetyxc.lingolearn.data.entity.TextData

@Dao
interface TextDataDao {
    @Query("SELECT * FROM text_data WHERE bookNameText = :bookName")
    suspend fun getTextBook(bookName: String): List<TextData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(textData: List<TextData>)
}