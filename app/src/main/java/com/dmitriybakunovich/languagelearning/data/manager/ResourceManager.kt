package com.dmitriybakunovich.languagelearning.data.manager

import android.content.Context

class ResourceManager(private val context: Context) {
    fun getDisplayPixels(): Int =
        (context.applicationContext.resources.displayMetrics.heightPixels -
                context.applicationContext.resources.displayMetrics.widthPixels)
    /*(context.applicationContext.resources.displayMetrics.xdpi +
            context.applicationContext.resources.displayMetrics.ydpi).toInt()*/

    fun getMovingPixels(): Int = (context.applicationContext.resources.displayMetrics.xdpi /
            context.applicationContext.resources.displayMetrics.scaledDensity).toInt()
}