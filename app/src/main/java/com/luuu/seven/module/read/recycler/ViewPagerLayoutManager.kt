package com.luuu.seven.module.read.recycler

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView


class ViewPagerLayoutManager : LinearLayoutManager {

    private val mPagerSnapHelper: PagerSnapHelper by lazy { PagerSnapHelper() }
    private var mOnViewPagerListener: OnViewPagerListener? = null
    private var mRecyclerView: RecyclerView? = null
    private var mDrift: Int = 0

    constructor(context: Context, orientation: Int): super(context, orientation, false)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean): super(context, orientation, reverseLayout)

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        mPagerSnapHelper.attachToRecyclerView(view)
        this.mRecyclerView = view
        mRecyclerView?.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener)
    }

    private val mChildAttachStateChangeListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewDetachedFromWindow(view: View) {
            if (mDrift >= 0) {
                mOnViewPagerListener?.onPageRelease(true, getPosition(view))
            } else {
                mOnViewPagerListener?.onPageRelease(false, getPosition(view))
            }
        }

        override fun onChildViewAttachedToWindow(view: View) {
        }
    }

    override fun onScrollStateChanged(state: Int) {
        when (state) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                val viewIdle = mPagerSnapHelper.findSnapView(this)
                val positionIdle = getPosition(viewIdle!!)
                if (mOnViewPagerListener != null && childCount == 1) {
                    mOnViewPagerListener?.onPageSelected(positionIdle, positionIdle == 0, positionIdle == itemCount - 1)
                }
            }
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                val viewDrag = mPagerSnapHelper.findSnapView(this)
                val positionDrag = getPosition(viewDrag!!)
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
                val viewSettling = mPagerSnapHelper.findSnapView(this)
                val positionSettling = getPosition(viewSettling!!)
            }
        }
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        mOnViewPagerListener?.onLayoutComplete()
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        this.mDrift = dy
        return super.scrollVerticallyBy(dy, recycler, state)
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        this.mDrift = dx
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    fun setOnViewPagerListener(listener: OnViewPagerListener) {
        this.mOnViewPagerListener = listener
    }
}