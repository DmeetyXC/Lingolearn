package com.dmeetyxc.lingolearn.ui.text

import android.view.GestureDetector
import android.view.MotionEvent

class TextGestureListener(private val touchListener: (TypeTouchText) -> Unit) :
    GestureDetector.SimpleOnGestureListener() {

    sealed class TypeTouchText {
        data class SingleTap(val event: MotionEvent) : TypeTouchText()
        object LongPress : TypeTouchText()
    }

    override fun onDown(event: MotionEvent): Boolean = true

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        touchListener(TypeTouchText.SingleTap(event = e))
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        touchListener(TypeTouchText.LongPress)
    }
}