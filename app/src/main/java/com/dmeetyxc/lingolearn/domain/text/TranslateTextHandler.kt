package com.dmeetyxc.lingolearn.domain.text

import com.dmeetyxc.lingolearn.BuildConfig
import com.dmeetyxc.lingolearn.data.entity.TranslationData
import com.dmeetyxc.lingolearn.data.network.TranslateService
import com.dmeetyxc.lingolearn.domain.settings.AppSettings
import javax.inject.Inject

class TranslateTextHandler @Inject constructor(
    private val translateService: TranslateService,
    private val appSettings: AppSettings
) {
    companion object {
        const val TRANSLATE_URL = "https://api.cognitive.microsofttranslator.com"
    }

    suspend fun translateText(textTranslate: String): String {
        val translateResult =
            translateService.getTranslateText(
                BuildConfig.apiTranslateText,
                appSettings.getMainLanguage()!!,
                appSettings.getChildLanguage()!!,
                listOf(TranslationData(textTranslate))
            )
        return translateResult[0].translation[0].text
    }
}