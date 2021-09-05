package com.dmeetyxc.lingolearn.di

import com.dmeetyxc.lingolearn.data.repository.BookRepositoryImpl
import com.dmeetyxc.lingolearn.data.repository.DictionaryRepositoryImpl
import com.dmeetyxc.lingolearn.data.repository.TextRepositoryImpl
import com.dmeetyxc.lingolearn.domain.book.BookRepository
import com.dmeetyxc.lingolearn.domain.dictionary.DictionaryRepository
import com.dmeetyxc.lingolearn.domain.text.TextRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    @ViewModelScoped
    fun bindBookRepository(bookRepositoryImpl: BookRepositoryImpl): BookRepository

    @Binds
    @ViewModelScoped
    fun bindTextRepository(textRepositoryImpl: TextRepositoryImpl): TextRepository

    @Binds
    @ViewModelScoped
    fun bindDictionaryRepository(
        dictionaryRepositoryImpl: DictionaryRepositoryImpl
    ): DictionaryRepository
}