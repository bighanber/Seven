package com.luuu.seven.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.viewpager.widget.ViewPager

class _TextWatcher : TextWatcher {

    private var _afterTextChanged: ((Editable?) -> Unit)? = null

    fun afterTextChanged(listener: (Editable?) -> Unit) {
        _afterTextChanged = listener
    }

    override fun afterTextChanged(s: Editable?) {
        _afterTextChanged?.invoke(s)
    }

    private var _beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null

    fun beforeTextChanged(listener: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit) {
        _beforeTextChanged = listener
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(s, start, count, after)
    }

    private var _onTextChanged: ((CharSequence, Int, Int, Int) -> Unit)? = null

    fun onTextChanged(listener: (s: CharSequence, start: Int, before: Int, count: Int) -> Unit) {
        _onTextChanged = listener
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(s, start, before, count)
    }

}

fun TextView.textChangedListener(init: _TextWatcher.() -> Unit) {
    val listener = _TextWatcher()
    listener.init()
    addTextChangedListener(listener)
}

class _ViewPageChangeListener : ViewPager.OnPageChangeListener {

    private var _onPageScrollStateChanged: ((Int) -> Unit)? = null

    fun onPageScrollStateChanged(listener: (Int) -> Unit) {
        _onPageScrollStateChanged = listener
    }

    override fun onPageScrollStateChanged(state: Int) {
        _onPageScrollStateChanged?.invoke(state)
    }

    private var _onPageScrolled: ((Int, Float, Int) -> Unit)? = null

    fun onPageScrolled(listener: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit) {
        _onPageScrolled = listener
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        _onPageScrolled?.invoke(position, positionOffset, positionOffsetPixels)
    }

    private var _onPageSelected: ((Int) -> Unit)? = null

    fun onPageSelected(listener: (Int) -> Unit) {
        _onPageSelected = listener
    }

    override fun onPageSelected(position: Int) {
        _onPageSelected?.invoke(position)
    }

}

fun ViewPager.pageChangeListener(init: _ViewPageChangeListener.() -> Unit) {
    val listener = _ViewPageChangeListener()
    listener.init()
    addOnPageChangeListener(listener)
}