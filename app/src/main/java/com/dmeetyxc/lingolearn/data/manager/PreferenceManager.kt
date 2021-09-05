package com.dmeetyxc.lingolearn.data.manager

interface PreferenceManager {

    fun saveLanguages(mainLanguage: String, childLanguage: String)

    fun getMainLanguage(): String?

    fun getChildLanguage(): String?

    fun getTextSize(): String?

    fun getAppTheme(): Int
}