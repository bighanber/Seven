package com.luuu.seven.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.AnimationUtils
import android.widget.Checkable
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.content.res.*
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import com.luuu.seven.R
import com.luuu.seven.util.*

class FilterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Checkable {

    companion object {
        private const val SELECTING_DURATION = 500L
        private const val DESELECTING_DURATION = 350L
    }

    private val outlinePaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private val textPaint by lazy { TextPaint(Paint.ANTI_ALIAS_FLAG) }
    private val dotPaint: Paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    private var selectedTextColor: Int = Color.WHITE

    var text: String = ""
        set(value) {
            field = value
            requestLayout()
        }

    private var progress = 0f
        set(value) {
            if (field != value) {
                field = value
                postInvalidateOnAnimation()
            }
        }

    private var showIcons: Boolean = true
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private val touchFeedback: Drawable
    @ColorInt
    private val defaultTextColor: Int
    private val clear: Drawable
    private val padding: Int
    private val cornerRadius: Float
    private lateinit var textLayout: StaticLayout
    private var progressAnimator: ValueAnimator? = null
    private var chipHeight = 0
    private val interp =
        AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in)

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.FilterView,
            defStyleAttr,
            R.style.FilterStyle
        )

        outlinePaint.apply {
            color = a.getColorOrThrow(R.styleable.FilterView_android_strokeColor)
            strokeWidth = a.getDimensionOrThrow(R.styleable.FilterView_outlineWidth)
            style = Paint.Style.STROKE
        }

        defaultTextColor = a.getColorOrThrow(R.styleable.FilterView_android_textColor)
        textPaint.apply {
            color = defaultTextColor
            textSize = a.getDimensionOrThrow(R.styleable.FilterView_android_textSize)
            letterSpacing = a.getFloat(R.styleable.FilterView_android_letterSpacing, 0f)
        }
        clear = a.getDrawableOrThrow(R.styleable.FilterView_clearIcon).apply {
            setBounds(
                -intrinsicWidth / 2,
                -intrinsicHeight / 2,
                intrinsicWidth / 2,
                intrinsicHeight / 2
            )
        }
        touchFeedback = a.getDrawableOrThrow(R.styleable.FilterView_foreground).apply {
            callback = this@FilterView
        }
        padding = a.getDimensionPixelSizeOrThrow(R.styleable.FilterView_android_padding)
        isChecked = a.getBoolean(R.styleable.FilterView_android_checked, false)
        showIcons = a.getBoolean(R.styleable.FilterView_showIcons, true)
        cornerRadius = a.getDimension(R.styleable.FilterView_cornerRadius, 0f)

        a.recycle()
        clipToOutline = true
        dotPaint.color = color(R.color.colorPrimary)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val nonTextWidth =
            (4 * padding) + (2 * outlinePaint.strokeWidth).toInt() + if (showIcons) clear.intrinsicWidth else 0
        val availableTextWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec) - nonTextWidth
            MeasureSpec.AT_MOST -> MeasureSpec.getSize(widthMeasureSpec) - nonTextWidth
            MeasureSpec.UNSPECIFIED -> 1000
            else -> 1000
        }

        createLayout(availableTextWidth)
        chipHeight = padding * 2 + textLayout.height
        val w = nonTextWidth + textLayout.textWidth()
        val h = chipHeight.coerceAtLeast(suggestedMinimumHeight)
        setMeasuredDimension(w, h)
        touchFeedback.setBounds(0, 0, w, chipHeight)
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val rounding = cornerRadius.coerceAtMost(chipHeight / 2f)
                val top = ((view.height - chipHeight) / 2).coerceAtLeast(0)
                outline.setRoundRect(0, top, view.width, top + chipHeight, rounding)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val ty = ((height - chipHeight) / 2f).coerceAtLeast(0f)
        canvas?.withTranslation(y = ty) {
            drawChip(this)
        }
    }

    private fun drawChip(canvas: Canvas) {
        val strokeWidth = outlinePaint.strokeWidth
        val iconRadius = clear.intrinsicWidth / 2
        val halfStroke = strokeWidth / 2f
        val rounding = cornerRadius.coerceAtMost((chipHeight - strokeWidth) / 2f)

        if (progress < 1f) {
            canvas.drawRoundRect(
                halfStroke,
                halfStroke,
                width - halfStroke,
                chipHeight - halfStroke,
                rounding,
                rounding,
                outlinePaint
            )
        }

        if (showIcons) {
            val dotRadius = lerp(0f, width.toFloat(), progress)
            canvas.drawCircle(
                width / 2f,
                chipHeight / 2f,
                dotRadius,
                dotPaint
            )
        } else {
            canvas.drawRoundRect(
                halfStroke,
                halfStroke,
                width - halfStroke,
                chipHeight - halfStroke,
                rounding,
                rounding,
                dotPaint
            )
        }

        val textLayoutDiff = (textLayout.width - textLayout.textWidth()) / 2
        val textBaseOffset = strokeWidth + padding
        val textAnimOffset = if (showIcons) {
            val offsetProgress = 1f - progress
            clear.intrinsicWidth * offsetProgress
        } else {
            0f
        }

        val textX = textBaseOffset + textAnimOffset - textLayoutDiff
        val selectedColor = selectedTextColor
        textPaint.color = if (selectedColor != 0 && progress > 0) {
            ColorUtils.blendARGB(defaultTextColor, selectedColor, progress)
        } else {
            defaultTextColor
        }
        canvas.withTranslation(x = textX, y = (chipHeight - textLayout.height) / 2f) {
            textLayout.draw(canvas)
        }

        if (showIcons && progress > 0f) {
            val iconX = width - strokeWidth - padding - iconRadius
            canvas.withTranslation(x = iconX, y = chipHeight / 2f) {
                canvas.withScale(progress, progress) {
                    clear.draw(canvas)
                }
            }
        }

        touchFeedback.draw(canvas)
    }

    fun animateCheckedAndInvoke(checked: Boolean, onEnd: (() -> Unit)?) {
        val newProgress = if (checked) 1f else 0f
        if (newProgress != progress) {
            progressAnimator?.cancel()
            progressAnimator = ValueAnimator.ofFloat(progress, newProgress).apply {
                addUpdateListener {
                    progress = it.animatedValue as Float
                }
                doOnEnd {
                    progress = newProgress
                    onEnd?.invoke()
                }
                interpolator = interp
                duration = if (checked) SELECTING_DURATION else DESELECTING_DURATION
                start()
            }
        }
    }

    private fun createLayout(textWidth: Int) {
        textLayout = newStaticLayout(
            text, textPaint, textWidth,
            Layout.Alignment.ALIGN_CENTER, 1f, 0f, true
        )
    }

    override fun isChecked(): Boolean = progress == 1f

    override fun toggle() {
        progress = if (progress == 0f) 1f else 0f
    }

    override fun setChecked(checked: Boolean) {
        progress = if (checked) 1f else 0f
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who == touchFeedback
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        touchFeedback.state = drawableState
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        touchFeedback.jumpToCurrentState()
    }

    override fun drawableHotspotChanged(x: Float, y: Float) {
        super.drawableHotspotChanged(x, y)
        touchFeedback.setHotspot(x, y)
    }
}