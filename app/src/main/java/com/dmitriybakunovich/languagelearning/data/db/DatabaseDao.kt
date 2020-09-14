package com.dmitriybakunovich.languagelearning.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.BookWithText
import com.dmitriybakunovich.languagelearning.data.db.entity.Dictionary
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM text_data")
    fun getAllTextData(): LiveData<List<TextData>>

    @Query("SELECT * FROM book_data")
    fun getAllBookData(): LiveData<List<BookData>>

    @Query("SELECT * FROM book_data WHERE bookCategory = :bookCategory")
    suspend fun loadBookDataCategory(bookCategory: String): List<BookData>

    @Query("SELECT * FROM book_data WHERE isFavourite")
    fun getFavoriteBook(): LiveData<List<BookData>>

    @Query("SELECT bookName FROM book_data")
    fun getBooksName(): List<String>

    @Query("SELECT * FROM book_data")
    fun getAllBookDataList(): List<BookData>

    @Transaction
    @Query("SELECT * FROM book_data")
    fun getBookWithText(): LiveData<List<BookWithText>>

    @Query("SELECT * FROM dictionary")
    fun getAllDictionary(): LiveData<List<Dictionary>>

    @Query("SELECT * FROM text_data WHERE bookNameText = :bookName")
    suspend fun getTextBook(bookName: String): List<TextData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(textData: TextData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookData: BookData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBooks(bookData: List<BookData>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(textData: List<TextData>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dictionary: Dictionary)

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