package com.dmeetyxc.lingolearn.data.manager

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.dmeetyxc.lingolearn.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceManager {

    override fun getDisplayPixels() = (context.resources.displayMetrics.xdpi +
            context.resources.displayMetrics.ydpi).toInt()

    override fun getMovingPixels() = (context.resources.displayMetrics.xdpi /
            context.resources.displayMetrics.scaledDensity).toInt()

    override fun getColorSelectText() =
        when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> ContextCompat.getColor(
                context, R.color.color_accent_night
            )
            Configuration.UI_MODE_NIGHT_YES -> ContextCompat.getColor(
                context, R.color.color_primary
            )
            else -> ContextCompat.getColor(context, R.color.color_primary)
        }

    override fun getLanguagesValue(): Array<String> =
        context.resources.getStringArray(R.array.languages_value)

    override fun getLanguages(): Array<String> =
        context.resources.getStringArray(R.array.languages)
}