package com.dmitriybakunovich.languagelearning.ui.choiceLanguage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository
import java.util.*

class ChoiceLanguageViewModel(private val repository: TextDataRepository) : ViewModel() {
    val childSelectState = MutableLiveData<Int>()
    private val selectedValues = repository.getListLanguages()

    fun saveLanguageChoice(mainLanguage: String, childLanguage: String) {
        repository.saveSelectLanguage(mainLanguage, childLanguage)
    }

    fun setInitialPositionLang() {
        val positionSystemLanguage =
            selectedValues.indexOfFirst { it == Locale.getDefault().language }
        childSelectState.postValue(positionSystemLanguage)
    }

    fun getSelectValues(position: Int) = selectedValues[position]
}