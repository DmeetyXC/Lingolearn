package com.dmeetyxc.lingolearn

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dmeetyxc.lingolearn.data.repository.TextDataRepository
import com.dmeetyxc.lingolearn.di.appModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startDi()
        setDayNightTheme()
    }

    private fun startDi() {
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    private fun setDayNightTheme() {
        val repository: TextDataRepository = get()
        AppCompatDelegate.setDefaultNightMode(repository.getAppTheme())
    }
}