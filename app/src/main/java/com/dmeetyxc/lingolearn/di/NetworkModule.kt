package com.dmeetyxc.lingolearn.di

import com.dmeetyxc.lingolearn.data.network.NetworkConnectionImpl
import com.dmeetyxc.lingolearn.domain.network.NetworkConnection
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface NetworkModule {

    @Binds
    @ViewModelScoped
    fun bindNetworkConnection(networkConnectionImpl: NetworkConnectionImpl): NetworkConnection
}