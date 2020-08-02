package com.dmitriybakunovich.languagelearning.di

import android.content.Context
import com.dmitriybakunovich.languagelearning.data.db.AppDatabase
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.manager.PreferenceManager
import com.dmitriybakunovich.languagelearning.data.manager.ResourceManager
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository
import com.dmitriybakunovich.languagelearning.ui.book.BookViewModel
import com.dmitriybakunovich.languagelearning.ui.dictionary.DictionaryViewModel
import com.dmitriybakunovich.languagelearning.ui.text.TextViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { BookViewModel(get()) }
    viewModel { (bookData: BookData) -> TextViewModel(bookData, get()) }
    viewModel { DictionaryViewModel(get()) }
    single { provideDatabaseDao(get()) }
    single { TextDataRepository(get(), ResourceManager(get()), PreferenceManager(get())) }
//    factory { CoroutineScope(Dispatchers.IO) }
}

fun provideDatabaseDao(context: Context) = AppDatabase
    .getDatabase(context)
    .databaseDao()