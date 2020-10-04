package com.dmitriybakunovich.languagelearning.data.manager

import android.content.Context
import androidx.core.content.ContextCompat
import com.dmitriybakunovich.languagelearning.R

class ResourceManager(private val context: Context) {
    fun getDisplayPixels(): Int =
        (context.applicationContext.resources.displayMetrics.xdpi +
                context.applicationContext.resources.displayMetrics.ydpi).toInt()
    /*(context.applicationContext.resources.displayMetrics.heightPixels -
            context.applicationContext.resources.displayMetrics.widthPixels)*/

    fun getMovingPixels(): Int = (context.applicationContext.resources.displayMetrics.xdpi /
            context.applicationContext.resources.displayMetrics.scaledDensity).toInt()

    fun getColorSelectText() =
        ContextCompat.getColor(context.applicationContext, R.color.colorPrimary)
}