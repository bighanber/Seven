package com.luuu.seven.widgets

import android.content.Context
import android.os.SystemClock
import android.text.format.DateFormat
import android.util.AttributeSet
import android.widget.TextView
import java.util.*



/**
 * Created by lls on 2017/8/7.
 */
class ClockText(context: Context, attrs: AttributeSet?, defStyle: Int) : TextView(context, attrs, defStyle) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        initClock()
    }

    private val FORMAT = "HH:mm"
    private var mCalendar: Calendar?= null
    private var mAttached = false

    private lateinit var mTicker: Runnable

    private fun initClock() {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance()
        }
        mTicker = Runnable {
            mCalendar!!.timeInMillis = System.currentTimeMillis()
            text = DateFormat.format(FORMAT, mCalendar)
            val now = SystemClock.uptimeMillis()
            val next = now + (1000 - now % 1000)
            handler.postAtTime(mTicker, next)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!mAttached) {
            mAttached = true
            mTicker.run()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mAttached) {
            handler.removeCallbacks(mTicker)
            mAttached = false
        }
    }
}