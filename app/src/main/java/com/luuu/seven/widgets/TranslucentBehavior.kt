package com.luuu.seven.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout


class TranslucentBehavior(private val context: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<Toolbar>(context, attrs) {

    private var mToolbarHeight = 0

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: Toolbar,
        dependency: View
    ): Boolean {
        return child is ImageView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: Toolbar,
        dependency: View
    ): Boolean {
        if (mToolbarHeight == 0) {
            mToolbarHeight = child.bottom * 2
        }
        var percent = dependency.y / mToolbarHeight

        if (percent >= 1) {
            percent = 1f
        }

        val alpha = percent * 255
        child.background.alpha = alpha.toInt()

        return true
    }
}