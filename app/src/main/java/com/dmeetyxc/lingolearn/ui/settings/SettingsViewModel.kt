package com.dmeetyxc.lingolearn.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmeetyxc.lingolearn.data.repository.TextDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: TextDataRepository) : ViewModel() {

    val childPreferenceSate = MutableLiveData<Pair<Array<String>, Array<String>>>()
    val childSelectItemSate = MutableLiveData<String>()
    private val languages = repository.getLanguages()
    private val languagesValues = repository.getLanguagesValue()

    fun initChildPref() {
        repository.getMainLanguage()?.let {
            childPreferenceSate.postValue(removeLanguage(it))
            childSelectItemSate.postValue(repository.getChildLanguage())
        }
    }

    fun handleMainClick() {
        repository.getMainLanguage()?.let {
            childPreferenceSate.postValue(removeLanguage(it))
            childSelectItemSate.postValue(repository.getChildLanguage())
            deleteAllData()
        }
    }

    fun deleteAllData() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllData()
    }

    private fun removeLanguage(item: String): Pair<Array<String>, Array<String>> {
        val resultLang = languages.toMutableList()
        val resultValue = languagesValues.toMutableList()
        languagesValues.forEachIndexed { index, element ->
            if (element == item) {
                resultValue.removeAt(index)
                resultLang.removeAt(index)
            }
        }
        return Pair(resultLang.toTypedArray(), resultValue.toTypedArray())
    }
}
