package com.dmitriybakunovich.languagelearning.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dmitriybakunovich.languagelearning.data.entity.BookData
import com.dmitriybakunovich.languagelearning.data.entity.Dictionary
import com.dmitriybakunovich.languagelearning.data.entity.TextData

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM book_data")
    fun getAllBookData(): LiveData<List<BookData>>

    @Query("SELECT * FROM book_data WHERE isFavourite")
    fun getFavoriteBook(): LiveData<List<BookData>>

    @Query("SELECT bookName FROM book_data")
    fun getBooksName(): List<String>

    @Query("SELECT * FROM dictionary")
    fun getAllDictionary(): LiveData<List<Dictionary>>

    @Query("SELECT * FROM text_data WHERE bookNameText = :bookName")
    suspend fun getTextBook(bookName: String): List<TextData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBooks(bookData: List<BookData>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(textData: List<TextData>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dictionary: Dictionary)

    @Update
    suspend fun update(bookData: BookData)

    @Delete
    suspend fun delete(bookData: BookData)

    @Query("DELETE FROM text_data")
    suspend fun deleteAllText()

    @Query("DELETE FROM book_data")
    suspend fun deleteAllBook()
}