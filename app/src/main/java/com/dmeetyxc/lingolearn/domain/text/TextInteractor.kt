package com.dmeetyxc.lingolearn.domain.text

import android.text.SpannableString
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.TextData

interface TextInteractor {

    fun searchNumberLineText(indexClick: Int, text: String): Int

    fun getTextSize(): Float?

    fun lineSelectedText(text: String, numberLine: Int): SpannableString

    suspend fun translateText(text: String): Result<Unit>

    suspend fun getTextData(bookData: BookData): List<TextData>

    fun updateBook(bookData: BookData)

    fun getMovingTextPixels(): Int
}