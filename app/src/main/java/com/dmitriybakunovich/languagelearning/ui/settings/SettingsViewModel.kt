package com.dmitriybakunovich.languagelearning.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: TextDataRepository) : ViewModel() {
    fun deleteAllData() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllData()
    }
}
