package com.dmeetyxc.lingolearn.di

import com.dmeetyxc.lingolearn.data.settings.AppSettingsImpl
import com.dmeetyxc.lingolearn.data.settings.TextSettingsImpl
import com.dmeetyxc.lingolearn.domain.settings.AppSettings
import com.dmeetyxc.lingolearn.domain.settings.TextSettings
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SettingsModule {

    @Binds
    fun bindAppSettings(appSettingsImpl: AppSettingsImpl): AppSettings

    @Binds
    fun bindTextSettings(textSettingsImpl: TextSettingsImpl): TextSettings
}