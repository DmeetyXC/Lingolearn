package com.dmitriybakunovich.languagelearning.di

import android.content.Context
import com.dmitriybakunovich.languagelearning.data.db.AppDatabase
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository
import com.dmitriybakunovich.languagelearning.ui.book.BookViewModel
import com.dmitriybakunovich.languagelearning.ui.text.TextViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { BookViewModel(get()) }
    viewModel { (bookData: BookData) -> TextViewModel(bookData, get()) }
    single { provideDatabaseDao(get(), get()) }
    single { TextDataRepository(get()) }
    factory { CoroutineScope(Dispatchers.IO) }
}

fun provideDatabaseDao(context: Context, scope: CoroutineScope) = AppDatabase
    .getDatabase(context, scope)
    .databaseDao()