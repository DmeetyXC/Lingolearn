package com.dmeetyxc.lingolearn.ui.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dmeetyxc.lingolearn.data.entity.Dictionary
import com.dmeetyxc.lingolearn.data.repository.TextDataRepository

class DictionaryViewModel(repository: TextDataRepository) : ViewModel() {
    val dictionary: LiveData<List<Dictionary>> = repository.dictionary
}