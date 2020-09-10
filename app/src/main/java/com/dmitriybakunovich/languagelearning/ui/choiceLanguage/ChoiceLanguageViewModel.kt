package com.dmitriybakunovich.languagelearning.ui.choiceLanguage

import androidx.lifecycle.ViewModel
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository

class ChoiceLanguageViewModel(private val repository: TextDataRepository) : ViewModel() {
    fun saveLanguageChoice(mainLanguage: String, childLanguage: String) {
        repository.saveSelectLanguage(mainLanguage, childLanguage)
    }
}