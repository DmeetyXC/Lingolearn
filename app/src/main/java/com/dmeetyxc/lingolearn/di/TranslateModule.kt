package com.dmeetyxc.lingolearn.di

import com.dmeetyxc.lingolearn.data.network.TranslateService
import com.dmeetyxc.lingolearn.domain.text.TranslateTextHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TranslateModule {

    @Provides
    @Singleton
    fun provideApiTranslate(): TranslateService = Retrofit.Builder()
        .baseUrl(TranslateTextHandler.TRANSLATE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TranslateService::class.java)
}