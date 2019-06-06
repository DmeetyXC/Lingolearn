package com.dmitriybakunovich.languagelearning

import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.widget.TextView
import androidx.lifecycle.ViewModel;

class BookViewModel : ViewModel() {

    fun searchFirstElement(indexClick: Int, text: String): Int {
        for (i in indexClick - 1 downTo 1) {
            val symbol1 = text[i]
            if (symbol1 == '.') {
                return i + 2
            }
        }
        return 0
    }

    fun searchLastElement(indexClick: Int, text: String): Int {
        for (i in indexClick + 1 until text.length) {
            val symbol2 = text[i]
            if (symbol2 == '.') {
                return i + 1
            }
        }
        return 0
    }

    fun highlightString(textView: TextView, firstIndex: Int, lastIndex: Int) {
        val spannableString = SpannableString(textView.text.toString())
        spannableString.setSpan(
            BackgroundColorSpan(-0x100), firstIndex,
            lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
    }
}
