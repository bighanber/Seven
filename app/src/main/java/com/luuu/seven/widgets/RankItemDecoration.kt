package com.luuu.seven.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.core.content.res.*
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.util.dp2px

class RankItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val paint by lazy { Paint() }
    private val textBounds by lazy { Rect() }
    private val headWidth: Int
    private var topThreeTextSize: Int
    private var normalTextSize: Int
    private var marginTop: Int

    init {
        val attrs = context.obtainStyledAttributes(R.style.RankStyle, R.styleable.RankHead)
        paint.apply {
            flags = Paint.ANTI_ALIAS_FLAG
            color = attrs.getColorOrThrow(R.styleable.RankHead_android_textColor)
            typeface = ResourcesCompat.getFont(context, attrs.getResourceIdOrThrow(R.styleable.RankHead_android_fontFamily))
        }
        headWidth = attrs.getDimensionPixelSizeOrThrow(R.styleable.RankHead_android_width)
        topThreeTextSize = attrs.getDimensionPixelSizeOrThrow(R.styleable.RankHead_topThreeTextSize)
        normalTextSize = attrs.getDimensionPixelSizeOrThrow(R.styleable.RankHead_normalTextSize)
        attrs.recycle()
        marginTop = context.dp2px(15)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        if (parent.isEmpty()) return

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i) ?: continue

            if (child.y > parent.height || (child.y + child.height) < 0) {
                continue
            }

            val position = parent.getChildAdapterPosition(child)
            if (position < 0) continue
            Log.v("asd", "positon: $position - childY: ${child.y} - parentH: ${parent.height} - childH: ${child.height} - childTop: ${child.top} - childBottom: ${child.bottom}")

            drawRankHeader(c, child, position, position < 4)
        }
    }

    private fun drawRankHeader(canvas: Canvas, child: View, pos: Int, isTopThree: Boolean) {
        paint.textSize = if (isTopThree) {
            topThreeTextSize.toFloat()
        } else {
            normalTextSize.toFloat()
        }

        paint.getTextBounds("$pos", 0, "$pos".length, textBounds)
        canvas.drawText("$pos", (headWidth - textBounds.width()).toFloat(), child.y + textBounds.height() + marginTop, paint)
    }
}