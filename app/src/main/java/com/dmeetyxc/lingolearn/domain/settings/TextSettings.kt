package com.dmeetyxc.lingolearn.domain.settings

interface TextSettings {

    fun getTextSize(): Float?

    /**
     * Available display size in pixels for determine the number of characters
     */
    fun getDisplayPixels(): Int

    /**
     * Value for moving navigate focus text after touch display
     */
    fun getMovingPixels(): Int

    fun getColorSelectText(): Int
}