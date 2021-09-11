package com.dmeetyxc.lingolearn.domain.network

import androidx.lifecycle.LiveData

interface NetworkConnection {

    fun fetchNetworkStatus(): LiveData<Boolean>
}