package com.dmeetyxc.lingolearn.ui.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dmeetyxc.lingolearn.data.entity.Dictionary
import com.dmeetyxc.lingolearn.data.repository.DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(repository: DictionaryRepository) : ViewModel() {
    val dictionary: LiveData<List<Dictionary>> = repository.getDictionary()
}