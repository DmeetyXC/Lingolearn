package com.dmeetyxc.lingolearn.di

import com.dmeetyxc.lingolearn.data.network.TranslateHandler
import com.dmeetyxc.lingolearn.data.network.TranslateService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiTranslate(): TranslateService = Retrofit.Builder()
        .baseUrl(TranslateHandler.TRANSLATE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TranslateService::class.java)
}