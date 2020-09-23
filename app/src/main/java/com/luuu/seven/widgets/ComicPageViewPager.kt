package com.luuu.seven.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class ComicPageViewPager : ViewPager {

    private var isLocked = false

    constructor(context: Context): super(context) {
        isLocked = false
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        isLocked = false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!isLocked) {
            return super.onInterceptTouchEvent(ev)
        }
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return !isLocked && super.onTouchEvent(ev)
    }

    fun setLocked(isLocked: Boolean) {
        this.isLocked = isLocked
    }
}