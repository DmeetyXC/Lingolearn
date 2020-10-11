package com.dmitriybakunovich.languagelearning

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dmitriybakunovich.languagelearning.data.repository.TextDataRepository
import com.dmitriybakunovich.languagelearning.di.appModule
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