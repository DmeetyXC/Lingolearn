package com.dmeetyxc.lingolearn.data.settings

import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import com.dmeetyxc.lingolearn.data.manager.ResourceManager
import com.dmeetyxc.lingolearn.domain.settings.AppSettings
import javax.inject.Inject

class AppSettingsImpl @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val resourceManager: ResourceManager
) : AppSettings {

    override fun saveLanguages(mainLanguage: String, childLanguage: String) =
        preferenceManager.saveLanguages(mainLanguage, childLanguage)

    override fun getMainLanguage(): String? = preferenceManager.getMainLanguage()

    override fun getChildLanguage(): String? = preferenceManager.getChildLanguage()

    override fun getAppTheme(): Int = preferenceManager.getAppTheme()

    override fun getLanguagesValue(): Array<String> = resourceManager.getLanguagesValue()

    override fun getLanguages(): Array<String> = resourceManager.getLanguages()
}