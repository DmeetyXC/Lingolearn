package com.dmeetyxc.lingolearn

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dmeetyxc.lingolearn.domain.settings.AppSettings
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    fun setDayNightTheme(appSettings: AppSettings) {
        AppCompatDelegate.setDefaultNightMode(appSettings.getAppTheme())
    }
}