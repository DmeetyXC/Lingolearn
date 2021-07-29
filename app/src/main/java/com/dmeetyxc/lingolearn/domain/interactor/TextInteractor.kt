package com.dmeetyxc.lingolearn.domain.interactor

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.Dictionary
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import com.dmeetyxc.lingolearn.data.manager.ResourceManager
import com.dmeetyxc.lingolearn.data.network.TranslateHandler
import com.dmeetyxc.lingolearn.data.repository.BooksRepository
import com.dmeetyxc.lingolearn.data.repository.DictionaryRepository
import com.dmeetyxc.lingolearn.data.repository.TextDataRepository
import javax.inject.Inject

class TextInteractor @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val booksRepository: BooksRepository,
    private val textDataRepository: TextDataRepository,
    private val translateHandler: TranslateHandler,
    private val resourceManager: ResourceManager,
    private val preferenceManager: PreferenceManager
) {

    fun searchNumberLineText(indexClick: Int, text: String): Int {
        var number = 0
        for (i in indexClick - 1 downTo 1) {
            val symbol1 = text[i]
            if (symbol1 == '.' || symbol1 == '!' || symbol1 == '?' || symbol1 == '…') {
                number++
            }
        }
        return number
    }

    fun getTextSize() = preferenceManager.getTextSize()?.toFloat()

    fun lineSelectedText(text: String, numberLine: Int): SpannableString {
        val firstIndex = getFirstElement(numberLine, text)
        val lastIndex = getLastElement(firstIndex, text)
        return selectionString(SpannableString(text), firstIndex, lastIndex)
    }

    suspend fun translateText(text: String) = kotlin.runCatching {
        val translateText = translateHandler.translateText(text)
        dictionaryRepository.insert(Dictionary(text, translateText))
    }

    suspend fun getTextData(bookData: BookData) = textDataRepository.getTextDataBook(bookData)

    fun updateBook(bookData: BookData) {
        booksRepository.updateBook(bookData)
    }

    fun getMovingTextPixels() = resourceManager.getMovingPixels()

    private fun selectionString(
        spannableString: SpannableString, firstIndex: Int, lastIndex: Int
    ): SpannableString {
        spannableString.setSpan(
            ForegroundColorSpan(resourceManager.getColorSelectText()), firstIndex,
            lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    private fun getFirstElement(numberLine: Int, text: String): Int {
        var counter = 0
        for (i in text.indices) {
            val symbol = text[i]
            if (symbol == '.' || symbol == '!' || symbol == '?' || symbol == '…') {
                counter++
            }
            if (counter == numberLine && counter == 0) {
                return i
            } else if (counter == numberLine) {
                return i + 1
            }
        }
        return counter
    }

    private fun getLastElement(firstIndex: Int, text: String): Int {
        var lastIndex = firstIndex
        for (i in firstIndex + 1 until text.length) {
            lastIndex++
            val symbol = text[i]
            if (symbol == '.' || symbol == '!' || symbol == '?' || symbol == '…') {
                return lastIndex + 1
            }
        }
        return lastIndex + 1
    }
}