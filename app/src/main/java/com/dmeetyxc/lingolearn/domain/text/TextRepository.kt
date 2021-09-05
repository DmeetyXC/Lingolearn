package com.dmeetyxc.lingolearn.domain.text

import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.TextData
import com.dmeetyxc.lingolearn.ui.text.BookType

interface TextRepository {

    suspend fun getTextDataBook(bookData: BookData): List<TextData>

    fun insert(textData: List<TextData>)

    suspend fun loadFullTextData(bookData: BookData, typeLoadBook: BookType): String
}