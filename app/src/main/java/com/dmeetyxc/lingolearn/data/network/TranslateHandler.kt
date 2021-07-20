package com.dmeetyxc.lingolearn.data.network

import com.dmeetyxc.lingolearn.BuildConfig
import com.dmeetyxc.lingolearn.data.entity.TranslationData
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import javax.inject.Inject

class TranslateHandler @Inject constructor(
    private val apiTranslate: ApiTranslate,
    private val preferenceManager: PreferenceManager,
) {
    companion object {
        const val TRANSLATE_URL = "https://api.cognitive.microsofttranslator.com"
    }

    suspend fun translateText(textTranslate: String): String {
        val translateResult =
            apiTranslate.getTranslateText(
                BuildConfig.apiTranslateText,
                preferenceManager.getMainLanguage()!!,
                preferenceManager.getChildLanguage()!!,
                listOf(TranslationData(textTranslate))
            )
        return translateResult[0].translation[0].text
    }
}