package com.dmeetyxc.lingolearn.ui.choiceLanguage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmeetyxc.lingolearn.data.repository.TextDataRepository
import com.dmeetyxc.lingolearn.util.SingleLiveEvent
import java.util.*

class ChoiceLanguageViewModel(private val repository: TextDataRepository) : ViewModel() {

    val childSelectState = MutableLiveData<Int>()
    val languageState = SingleLiveEvent<Boolean>()
    private val selectedValues = repository.getLanguagesValue()

    init {
        languageState.postValue(checkSaveLanguage())
        setInitialPositionLang()
    }

    fun saveLanguageChoice(mainLanguage: String, childLanguage: String) {
        repository.saveSelectLanguage(mainLanguage, childLanguage)
    }

    fun getSelectValues(position: Int) = selectedValues[position]

    private fun checkSaveLanguage(): Boolean {
        val mainLanguage = repository.getMainLanguage()
        return mainLanguage != null && mainLanguage.isEmpty()
    }

    private fun setInitialPositionLang() {
        val positionSystemLanguage =
            selectedValues.indexOfFirst { it == Locale.getDefault().language }
        childSelectState.postValue(positionSystemLanguage)
    }
}