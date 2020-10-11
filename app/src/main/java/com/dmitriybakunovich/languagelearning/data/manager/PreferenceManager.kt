package com.dmitriybakunovich.languagelearning.data.manager

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class PreferenceManager(private val sharedPref: SharedPreferences) {

    fun saveLanguage(mainLanguage: String, childLanguage: String) {
        with(sharedPref.edit()) {
            putString("main", mainLanguage)
            putString("child", childLanguage)
            apply()
        }
    }

    fun loadMainLanguage(): String? {
        return sharedPref.getString("main", "")
    }

    fun loadChildLanguage(): String? {
        return sharedPref.getString("child", "")
    }

    fun loadTextSize(): String? {
        return sharedPref.getString("font_size", "16")
    }

    fun getAppTheme(): Int = when (sharedPref.getString("theme", "system")) {
        "light" -> AppCompatDelegate.MODE_NIGHT_NO
        "dark" -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}