package com.dmitriybakunovich.languagelearning.data.manager

import android.content.Context
import java.util.*

class PreferenceManager(private val context: Context) {

    fun saveLanguage(mainLanguage: String, childLanguage: String) {
        val sharedPref = context.getSharedPreferences(
            "language", Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString("main", mainLanguage)
            putString("child", childLanguage)
            commit()
        }
    }

    fun loadMainLanguage(): String? {
        val sharedPref = context.getSharedPreferences(
            "language", Context.MODE_PRIVATE
        )
        return sharedPref.getString("main", "")
    }

    fun loadChildLanguage(): String? {
        val sharedPref = context.getSharedPreferences(
            "language", Context.MODE_PRIVATE
        )
        return sharedPref.getString("child", Locale.getDefault().language)
    }
}