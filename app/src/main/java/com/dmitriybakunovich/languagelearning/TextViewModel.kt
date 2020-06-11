package com.dmitriybakunovich.languagelearning

import android.app.Application
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dmitriybakunovich.languagelearning.data.db.AppDatabase
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository

class TextViewModel(application: Application) : AndroidViewModel(application) {
    // Required to receive a dedicated offer for further translation
    // To select text you only need textLineSelected
    private var textSelectedMain = MutableLiveData<SpannableString>()
    private var textSelectedChild = MutableLiveData<SpannableString>()
    var textLineSelected = MutableLiveData<Int>()
    var scrollTextState = MutableLiveData<Int>()

    private val repository: TextDataRepository
    val allText: LiveData<List<TextData>>

    init {
        val databaseDao = AppDatabase.getDatabase(application, viewModelScope).databaseDao()
        repository =
            TextDataRepository(
                databaseDao
            )
        allText = repository.allTextData
    }

    private fun selectSetTextMain(text: SpannableString) {
        textSelectedMain.postValue(text)
    }

    private fun selectSetTextChild(text: SpannableString) {
        textSelectedChild.postValue(text)
    }

    private fun selectLineText(line: Int) {
        textLineSelected.postValue(line)
    }

    fun touchText(offset: Int, text: String, touchType: TextTouchType) {
        val firstElement = searchFirstElement(offset, text)
        val lastElement = searchLastElement(offset, text)
        val spannableString = selectionString(
            SpannableString(text),
            firstElement, lastElement
        )
        when (touchType) {
            TextTouchType.MAIN -> selectSetTextMain(spannableString)
            TextTouchType.CHILD -> selectSetTextChild(spannableString)
        }
    }

    private fun searchFirstElement(indexClick: Int, text: String): Int {
        for (i in indexClick - 1 downTo 1) {
            val symbol1 = text[i]
            if (symbol1 == '.' || symbol1 == '!' || symbol1 == '?') {
                return i + 2
            }
        }
        return 0
    }

    private fun searchLastElement(indexClick: Int, text: String): Int {
        for (i in indexClick + 1 until text.length) {
            val symbol2 = text[i]
            if (symbol2 == '.' || symbol2 == '!' || symbol2 == '?') {
                return i + 1
            }
        }
        return 0
    }

    private fun selectionString(
        spannableString: SpannableString, firstIndex: Int, lastIndex: Int
    ): SpannableString {
//        BackgroundColorSpan(Color.GREEN)
        spannableString.setSpan(
            ForegroundColorSpan(Color.GREEN), firstIndex,
            lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun searchNumberLineText(indexClick: Int, text: String) {
        var number = 0
        for (i in indexClick - 1 downTo 1) {
            val symbol1 = text[i]
            if (symbol1 == '.' || symbol1 == '!' || symbol1 == '?') {
                number++
            }
        }
        selectLineText(number)
    }

    fun handleLineSelected(text: String, numberLine: Int): SpannableString {
        val firstIndex = getFirstElement(numberLine, text)
        val lastIndex = getLastElement(firstIndex, text)
        return selectionString(SpannableString(text), firstIndex, lastIndex)
    }

    private fun getFirstElement(numberLine: Int, text: String): Int {
        var counter = 0
        for (i in text.indices) {
            val symbol = text[i]
            if (symbol == '.' || symbol == '!' || symbol == '?') {
                counter++
            }
            if (counter == numberLine) {
                return i
            }
        }
        return counter
    }

    private fun getLastElement(firstIndex: Int, text: String): Int {
        var lastIndex = firstIndex
        for (i in firstIndex + 1 until text.length) {
            lastIndex++
            val symbol = text[i]
            if (symbol == '.' || symbol == '!' || symbol == '?') {
                return lastIndex + 1
            }
        }
        return lastIndex + 1
    }

    fun scrollTextPosition(lineTwain: Int, line: Int) {
        if (lineTwain <= line + 2) {
            scrollTextState.postValue(lineTwain + 100)
        }
    }
}
