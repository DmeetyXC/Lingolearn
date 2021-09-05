package com.dmeetyxc.lingolearn.di

import com.dmeetyxc.lingolearn.domain.book.BookInteractor
import com.dmeetyxc.lingolearn.domain.book.BookInteractorImpl
import com.dmeetyxc.lingolearn.domain.text.TextInteractor
import com.dmeetyxc.lingolearn.domain.text.TextInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface InteractorModule {

    @Binds
    @ViewModelScoped
    fun bindBookInteractor(bookInteractorImpl: BookInteractorImpl): BookInteractor

    @Binds
    @ViewModelScoped
    fun bindTextInteractor(textInteractorImpl: TextInteractorImpl): TextInteractor
}