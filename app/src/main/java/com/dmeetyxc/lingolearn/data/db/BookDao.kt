package com.dmeetyxc.lingolearn.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dmeetyxc.lingolearn.data.entity.BookData

@Dao
interface BookDao {
    @Query("SELECT * FROM book_data")
    fun getAllBookData(): LiveData<List<BookData>>

    @Query("SELECT * FROM book_data WHERE isFavourite")
    fun getFavoriteBook(): LiveData<List<BookData>>

    @Query("SELECT bookName FROM book_data")
    fun getBooksName(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBooks(bookData: List<BookData>)

    @Update
    suspend fun update(bookData: BookData)

    @Transaction
    suspend fun deleteBooks() {
        deleteAllText()
        deleteAllBook()
    }

    @Query("DELETE FROM text_data")
    suspend fun deleteAllText()

    @Query("DELETE FROM book_data")
    suspend fun deleteAllBook()
}