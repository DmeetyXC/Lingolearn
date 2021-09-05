package com.dmeetyxc.lingolearn.domain.settings

interface AppSettings {

    /**
     * Save the studied and native languages
     */
    fun saveLanguages(mainLanguage: String, childLanguage: String)

    fun getMainLanguage(): String?

    fun getChildLanguage(): String?

    fun getAppTheme(): Int

    fun getLanguagesValue(): Array<String>

    fun getLanguages(): Array<String>
}