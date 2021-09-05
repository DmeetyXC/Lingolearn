package com.dmeetyxc.lingolearn.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmeetyxc.lingolearn.domain.book.BookRepository
import com.dmeetyxc.lingolearn.domain.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val appSettings: AppSettings
) : ViewModel() {

    val childPreferenceSate = MutableLiveData<Pair<Array<String>, Array<String>>>()
    val childSelectItemSate = MutableLiveData<String>()
    private val languages = appSettings.getLanguages()
    private val languagesValues = appSettings.getLanguagesValue()

    fun initChildPref() {
        appSettings.getMainLanguage()?.let {
            childPreferenceSate.postValue(removeLanguage(it))
            childSelectItemSate.postValue(appSettings.getChildLanguage())
        }
    }

    fun handleMainClick() {
        appSettings.getMainLanguage()?.let {
            childPreferenceSate.postValue(removeLanguage(it))
            childSelectItemSate.postValue(appSettings.getChildLanguage())
            deleteAllData()
        }
    }

    fun deleteAllData() = viewModelScope.launch(Dispatchers.IO) {
        bookRepository.deleteAllBooks()
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
