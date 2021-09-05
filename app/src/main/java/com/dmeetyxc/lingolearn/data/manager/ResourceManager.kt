package com.dmeetyxc.lingolearn.data.manager

interface ResourceManager {

    fun getDisplayPixels(): Int

    fun getMovingPixels(): Int

    fun getColorSelectText(): Int

    fun getLanguagesValue(): Array<String>

    fun getLanguages(): Array<String>
}