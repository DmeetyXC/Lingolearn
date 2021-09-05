package com.dmeetyxc.lingolearn.ui.choiceLanguage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmeetyxc.lingolearn.domain.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChoiceLanguageViewModel @Inject constructor(
    private val appSettings: AppSettings
) : ViewModel() {

    val childSelectState = MutableLiveData<Int>()
    private val selectedValues = appSettings.getLanguagesValue()

    init {
        setInitialPositionLang()
    }

    fun saveLanguageChoice(mainLanguage: String, childLanguage: String) {
        appSettings.saveLanguages(mainLanguage, childLanguage)
    }

    fun getSelectValues(position: Int) = selectedValues[position]

    private fun setInitialPositionLang() {
        val positionSystemLanguage =
            selectedValues.indexOfFirst { it == Locale.getDefault().language }
        childSelectState.postValue(positionSystemLanguage)
    }
}