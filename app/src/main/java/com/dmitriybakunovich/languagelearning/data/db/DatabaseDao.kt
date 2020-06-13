package com.dmitriybakunovich.languagelearning.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.BookWithText
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM text_data")
    fun getAllTextData(): LiveData<List<TextData>>

    @Transaction
    @Query("SELECT * FROM book_data")
    fun getBookWithText(): LiveData<List<BookWithText>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(textData: TextData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookData: BookData)

    @Update
    suspend fun update(textData: TextData)

    @Update
    suspend fun update(bookData: BookData)

    @Delete
    suspend fun delete(textData: TextData)

    @Delete
    suspend fun delete(bookData: BookData)

    @Query("DELETE FROM text_data")
    suspend fun deleteAll()
}