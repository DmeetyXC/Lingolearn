package com.dmeetyxc.lingolearn.di

import android.content.Context
import android.content.SharedPreferences
import com.dmeetyxc.lingolearn.data.db.AppDatabase
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.manager.ConnectionManager
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import com.dmeetyxc.lingolearn.data.manager.ResourceManager
import com.dmeetyxc.lingolearn.data.repository.TextDataRepository
import com.dmeetyxc.lingolearn.ui.book.BookViewModel
import com.dmeetyxc.lingolearn.ui.choiceLanguage.ChoiceLanguageViewModel
import com.dmeetyxc.lingolearn.ui.dictionary.DictionaryViewModel
import com.dmeetyxc.lingolearn.ui.favorite.FavoriteViewModel
import com.dmeetyxc.lingolearn.ui.settings.SettingsViewModel
import com.dmeetyxc.lingolearn.ui.text.TextViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { BookViewModel(get(), ConnectionManager(get())) }
    viewModel { (bookData: BookData) -> TextViewModel(bookData, get()) }
    viewModel { DictionaryViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { ChoiceLanguageViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    single { provideDatabaseDao(get()) }
    single {
        TextDataRepository(
            get(), PreferenceManager(provideSharedPreferences(get())), ResourceManager(get())
        )
    }
//    factory { CoroutineScope(Dispatchers.IO) }
}

fun provideDatabaseDao(context: Context) = AppDatabase
    .getDatabase(context)
    .databaseDao()

fun provideSharedPreferences(context: Context): SharedPreferences =
    androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)