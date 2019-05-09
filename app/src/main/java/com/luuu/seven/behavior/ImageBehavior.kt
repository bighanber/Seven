package com.luuu.seven.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

/**
 * Created by lls on 2017/8/7.
 *
 */
class ImageBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<ImageView>(context, attrs) {

    private val TAG = "toolbar"
    private var mStartAvatarY = 0f
    private var mStartAvatarX = 0f
    private var mToolBarHeight = 0
    private var percent = 0f

    override fun layoutDependsOn(parent: CoordinatorLayout, child: ImageView, dependency: View): Boolean {
        return dependency is LinearLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ImageView, dependency: View): Boolean {
        init(parent, child)
        if (child.y <= 0) return false
        var percent = (child.y - mToolBarHeight) / (mStartAvatarY - mToolBarHeight)

        if (percent < 0) {
            percent = 0f
        }
        if (this.percent == percent || percent > 1) return true
        this.percent = percent
        child.scaleX = percent
        child.scaleY = percent

        return false
    }

    private fun init(parent: CoordinatorLayout, child: ImageView) {
        if (mStartAvatarY == 0f) {
            mStartAvatarY = child.y
        }
        if (mStartAvatarX == 0f) {
            mStartAvatarX = child.x
        }
        if (mToolBarHeight == 0) {
            val toolbar = parent.findViewWithTag<Toolbar>(TAG)
            mToolBarHeight = toolbar.height
        }
    }
}