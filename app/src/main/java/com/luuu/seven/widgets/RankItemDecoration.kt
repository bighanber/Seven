package com.luuu.seven.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.res.*
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.util.color
import com.luuu.seven.util.dp2px

class RankItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val paint by lazy { Paint() }
    private val textBounds by lazy { Rect() }
    private val headWidth: Int
    private var topThreeTextSize: Int
    private var normalTextSize: Int
    private var marginTop: Int
    private var paddingStart = 0
    private var mContext: Context
    private val colors = arrayListOf(R.color.champion, R.color.runnerUp, R.color.thirdPlace)
    private var normalColor = 0

    init {
        val attrs = context.obtainStyledAttributes(R.style.RankStyle, R.styleable.RankHead)
        normalColor = attrs.getColorOrThrow(R.styleable.RankHead_android_textColor)
        paint.apply {
            flags = Paint.ANTI_ALIAS_FLAG
            color = normalColor
            typeface = ResourcesCompat.getFont(context, attrs.getResourceIdOrThrow(R.styleable.RankHead_android_fontFamily))
        }
        headWidth = attrs.getDimensionPixelSizeOrThrow(R.styleable.RankHead_android_width)
        topThreeTextSize = attrs.getDimensionPixelSizeOrThrow(R.styleable.RankHead_topThreeTextSize)
        normalTextSize = attrs.getDimensionPixelSizeOrThrow(R.styleable.RankHead_normalTextSize)
        attrs.recycle()
        marginTop = context.dp2px(15)
        mContext = context
        paddingStart = context.dp2px(10)
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
            drawRankHeader(c, child, position + 1, position < 3)
        }
    }

    private fun drawRankHeader(canvas: Canvas, child: View, pos: Int, isTopThree: Boolean) {
        if (isTopThree) {
            paint.textSize = topThreeTextSize.toFloat()
            paint.color = mContext.color(colors[pos - 1])
        } else {
            paint.textSize = normalTextSize.toFloat()
            paint.color = normalColor
        }

        paint.getTextBounds("$pos", 0, "$pos".length, textBounds)
        canvas.drawText("$pos", (headWidth - textBounds.width() + paddingStart).toFloat(), child.y + textBounds.height() + marginTop, paint)
    }
}