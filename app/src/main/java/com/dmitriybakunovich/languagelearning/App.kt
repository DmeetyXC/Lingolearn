package com.dmitriybakunovich.languagelearning

import android.app.Application
import com.dmitriybakunovich.languagelearning.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startDi()
    }

    private fun startDi() {
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}