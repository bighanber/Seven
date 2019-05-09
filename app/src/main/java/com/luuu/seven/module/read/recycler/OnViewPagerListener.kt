package com.luuu.seven.module.read.recycler

/**
 * Created by lls on 2018/9/1
 */
interface OnViewPagerListener {

    fun onPageRelease(isNext: Boolean, position: Int)

    fun onPageSelected(position: Int, isTop: Boolean, isBottom: Boolean)

    fun onLayoutComplete()
}