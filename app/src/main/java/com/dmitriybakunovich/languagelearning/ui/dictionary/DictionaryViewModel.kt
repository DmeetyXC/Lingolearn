package com.dmitriybakunovich.languagelearning.ui.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dmitriybakunovich.languagelearning.data.db.entity.Dictionary
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository

class DictionaryViewModel(repository: TextDataRepository) : ViewModel() {
    val dictionary: LiveData<List<Dictionary>> = repository.dictionary
}