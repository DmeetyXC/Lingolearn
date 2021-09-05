package com.dmeetyxc.lingolearn.domain.book

import androidx.lifecycle.LiveData
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.BookParentModel

interface BookInteractor {

    suspend fun checkNewBooks()

    fun getCategoryBook(): LiveData<List<BookParentModel>>

    suspend fun loadBook(bookData: BookData)

    fun checkSaveLanguage(): Boolean

    suspend fun updateFavoriteBook(book: BookData)
}