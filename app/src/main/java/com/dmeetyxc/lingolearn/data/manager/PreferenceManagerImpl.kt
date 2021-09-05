package com.dmeetyxc.lingolearn.data.manager

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject

class PreferenceManagerImpl @Inject constructor(
    private val sharedPref: SharedPreferences
) : PreferenceManager {

    companion object {
        const val MAIN = "main"
        const val CHILD = "child"
        const val FONT_SIZE = "font_size"
        const val FONT_SIZE_DEFAULT = "16"
        const val THEME = "theme"
        const val THEME_DEFAULT = "system"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val BOOK = "book"
    }

    override fun saveLanguages(mainLanguage: String, childLanguage: String) {
        with(sharedPref.edit()) {
            putString(MAIN, mainLanguage)
            putString(CHILD, childLanguage)
            apply()
        }
    }

    override fun getMainLanguage() = sharedPref.getString(MAIN, "")

    override fun getChildLanguage() = sharedPref.getString(CHILD, "")

    override fun getTextSize(): String? = sharedPref.getString(FONT_SIZE, FONT_SIZE_DEFAULT)

    override fun getAppTheme(): Int = when (sharedPref.getString(THEME, THEME_DEFAULT)) {
        THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}