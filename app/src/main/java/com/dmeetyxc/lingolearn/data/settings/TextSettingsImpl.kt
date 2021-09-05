package com.dmeetyxc.lingolearn.data.settings

import com.dmeetyxc.lingolearn.data.manager.PreferenceManager
import com.dmeetyxc.lingolearn.data.manager.ResourceManager
import com.dmeetyxc.lingolearn.domain.settings.TextSettings
import javax.inject.Inject

class TextSettingsImpl @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val resourceManager: ResourceManager
) : TextSettings {

    override fun getTextSize(): Float? = preferenceManager.getTextSize()?.toFloat()

    override fun getDisplayPixels(): Int = resourceManager.getDisplayPixels()

    override fun getMovingPixels(): Int = resourceManager.getMovingPixels()

    override fun getColorSelectText(): Int = resourceManager.getColorSelectText()
}