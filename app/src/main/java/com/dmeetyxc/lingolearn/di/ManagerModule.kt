package com.dmeetyxc.lingolearn.di

import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import com.dmeetyxc.lingolearn.data.manager.PreferenceManagerImpl
import com.dmeetyxc.lingolearn.data.manager.ResourceManager
import com.dmeetyxc.lingolearn.data.manager.ResourceManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ManagerModule {

    @Binds
    fun bindPreferenceManager(preferenceManagerImpl: PreferenceManagerImpl): PreferenceManager

    @Binds
    fun bindResourceManager(resourceManagerImpl: ResourceManagerImpl): ResourceManager
}