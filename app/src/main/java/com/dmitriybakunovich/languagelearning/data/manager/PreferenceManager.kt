package com.dmitriybakunovich.languagelearning.data.manager

import android.content.SharedPreferences

class PreferenceManager(private val sharedPref: SharedPreferences) {

    fun saveLanguage(mainLanguage: String, childLanguage: String) {
        with(sharedPref.edit()) {
            putString("main", mainLanguage)
            putString("child", childLanguage)
            commit()
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
}