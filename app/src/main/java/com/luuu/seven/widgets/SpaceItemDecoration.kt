package com.luuu.seven.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class SpaceItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val mDividerPaint = Paint()
    private val mDisplayMetrics: DisplayMetrics = context.resources.displayMetrics
    private var mSpace: Int = 0
    private var mEdgeSpace: Int = 0

    fun setSpace(space: Int): SpaceItemDecoration {
        mSpace = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, space.toFloat(), mDisplayMetrics) + 0.5f).toInt()
        return this
    }

    fun setEdgeSpace(edgeSpace: Int): SpaceItemDecoration {

        mEdgeSpace = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, edgeSpace.toFloat(), mDisplayMetrics) + 0.5f).toInt()
        mDividerPaint.color = Color.RED
        return this
    }

    fun setSpaceColor(spaceColor: Int): SpaceItemDecoration {
        mDividerPaint.color = spaceColor
        return this
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val manager = parent.layoutManager
        val childPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount
        if (manager != null) {
            if (manager is GridLayoutManager) {
                setGrid(manager.orientation, manager.spanCount, outRect, childPosition, itemCount ?: 0)
            } else if (manager is LinearLayoutManager) {
                setLinear(manager .orientation, outRect, childPosition, itemCount ?: 0)
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val manager = parent.layoutManager
        if (manager != null) {
            if (manager is GridLayoutManager) {

            } else if (manager is LinearLayoutManager) {
                drawLinear(manager.orientation, c, parent)
            }
        }
    }

    private fun setLinear(orientation: Int, outRect: Rect, childPosition: Int, itemCount: Int) {
        if (orientation == LinearLayoutManager.VERTICAL) {
            when (childPosition) {
                0 -> outRect.set(0, mEdgeSpace, 0, mSpace)
                itemCount - 1 -> outRect.set(0, 0, 0, mEdgeSpace)
                else -> outRect.set(0, 0, 0, mSpace)
            }
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            when (childPosition) {
                0 -> outRect.set(mEdgeSpace, 0, mSpace, 0)
                itemCount - 1 -> outRect.set(0, 0, mEdgeSpace, 0)
                else -> outRect.set(0, 0, mSpace, 0)
            }
        }
    }

    private fun setGrid(orientation: Int, spanCount: Int, outRect: Rect, childPosition: Int, itemCount: Int) {

        val totalSpace = (mSpace * (spanCount - 1) + mEdgeSpace * 2).toFloat()//总共的间距

        val eachSpace = totalSpace / spanCount
        val column = childPosition % spanCount
        val row = childPosition / spanCount

        var left: Float
        var right: Float
        val top: Float
        var bottom: Float
        if (orientation == GridLayoutManager.VERTICAL) {
            top = 0f
            bottom = mSpace.toFloat()

            if (itemCount % spanCount != 0 && itemCount / spanCount == row || itemCount % spanCount == 0 && itemCount / spanCount == row + 1) {
                bottom = mEdgeSpace.toFloat()
            }

            if (spanCount == 1) {
                left = mEdgeSpace.toFloat()
                right = left
            } else {
                left = column * (eachSpace - mEdgeSpace.toFloat() - mEdgeSpace.toFloat()) / (spanCount - 1) + mEdgeSpace
                right = eachSpace - left
            }
        } else {
            left = 0f
            right = mSpace.toFloat()

            if (childPosition < spanCount) {
                left = mEdgeSpace.toFloat()
            }
            if (itemCount % spanCount != 0 && itemCount / spanCount == row || itemCount % spanCount == 0 && itemCount / spanCount == row + 1) {
                right = mEdgeSpace.toFloat()
            }

            if (spanCount == 1) {
                top = mEdgeSpace.toFloat()
                bottom = top
            } else {
                top = column * (eachSpace - mEdgeSpace.toFloat() - mEdgeSpace.toFloat()) / (spanCount - 1) + mEdgeSpace
                bottom = eachSpace - top
            }
        }

        outRect.set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }


    private fun drawLinear(orientation: Int, c: Canvas, parent: RecyclerView) {
        if (orientation == LinearLayoutManager.VERTICAL) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin +
                        ViewCompat.getTranslationY(child).roundToInt()
                val bottom = top + mSpace
                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mDividerPaint)
            }

        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            val top = parent.paddingTop
            val bottom = parent.height - parent.paddingBottom
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val left = child.right + params.rightMargin +
                        ViewCompat.getTranslationX(child).roundToInt()
                val right = left + mSpace
                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mDividerPaint)
            }
        }
    }
}