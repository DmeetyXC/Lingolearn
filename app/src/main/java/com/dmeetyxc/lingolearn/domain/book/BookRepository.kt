package com.dmeetyxc.lingolearn.domain.book

import androidx.lifecycle.LiveData
import com.dmeetyxc.lingolearn.data.entity.BookData

interface BookRepository {

    fun getAllBookData(): LiveData<List<BookData>>

    suspend fun addNewBooks(childLanguage: String)

    suspend fun deleteAllBooks()

    fun getBooksNameLocal(): List<String>

    fun getFavoriteBook(): LiveData<List<BookData>>

    fun updateBook(bookData: BookData)

    suspend fun loadBooksNameCloud(): List<String>
}