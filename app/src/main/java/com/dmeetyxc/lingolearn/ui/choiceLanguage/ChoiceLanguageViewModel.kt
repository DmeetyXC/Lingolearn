package com.dmeetyxc.lingolearn.ui.choiceLanguage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import com.dmeetyxc.lingolearn.data.manager.ResourceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChoiceLanguageViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    resourceManager: ResourceManager
) : ViewModel() {

    val childSelectState = MutableLiveData<Int>()
    private val selectedValues = resourceManager.getLanguagesValue()

    init {
        setInitialPositionLang()
    }

    fun saveLanguageChoice(mainLanguage: String, childLanguage: String) {
        preferenceManager.saveLanguages(mainLanguage, childLanguage)
    }

    fun getSelectValues(position: Int) = selectedValues[position]

    private fun setInitialPositionLang() {
        val positionSystemLanguage =
            selectedValues.indexOfFirst { it == Locale.getDefault().language }
        childSelectState.postValue(positionSystemLanguage)
    }
}