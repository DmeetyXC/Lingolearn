package com.dmeetyxc.lingolearn

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    fun setDayNightTheme(preferenceManager: PreferenceManager) {
        AppCompatDelegate.setDefaultNightMode(preferenceManager.getAppTheme())
    }
}