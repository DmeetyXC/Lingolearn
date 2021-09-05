package com.dmeetyxc.lingolearn.data.network

import androidx.lifecycle.LiveData

interface NetworkConnection {

    fun fetchNetworkStatus(): LiveData<Boolean>
}