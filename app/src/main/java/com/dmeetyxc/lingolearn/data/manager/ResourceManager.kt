package com.dmeetyxc.lingolearn.data.manager

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.dmeetyxc.lingolearn.R

class ResourceManager(private val context: Context) {

    fun getDisplayPixels() = (context.applicationContext.resources.displayMetrics.xdpi +
            context.applicationContext.resources.displayMetrics.ydpi).toInt()
    /*(context.applicationContext.resources.displayMetrics.heightPixels -
            context.applicationContext.resources.displayMetrics.widthPixels)*/

    fun getMovingPixels() = (context.applicationContext.resources.displayMetrics.xdpi /
            context.applicationContext.resources.displayMetrics.scaledDensity).toInt()

    fun getColorSelectText() =
        when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> ContextCompat.getColor(
                context.applicationContext, R.color.color_accent_night
            )
            Configuration.UI_MODE_NIGHT_YES -> ContextCompat.getColor(
                context.applicationContext, R.color.color_primary
            )
            else -> ContextCompat.getColor(context.applicationContext, R.color.color_primary)
        }

    fun getLanguagesValue(): Array<String> =
        context.applicationContext.resources.getStringArray(R.array.languages_value)

    fun getLanguages(): Array<String> =
        context.applicationContext.resources.getStringArray(R.array.languages)
}