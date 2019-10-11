package com.luuu.seven.widgets

import android.content.Context
import android.graphics.Color
import android.text.Layout
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.animation.Animation
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.luuu.seven.util.newStaticLayout

class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        private const val ELLIPSIS_STRING = """\u2026"""
        private const val DEFAULT_OPEN_SUFFIX = " 展开"
        private const val DEFAULT_CLOSE_SUFFIX = " 收起"
        private const val DEFAULT_MAX_LINE = 3
    }

    private var isClosed = false
    private var mMaxLines = DEFAULT_MAX_LINE
    private var initWidth = 0
    private var originalText: CharSequence? = null
    private var mOpenSpannableStr: SpannableStringBuilder? = null
    private var mCloseSpannableStr: SpannableStringBuilder? = null
    private var hasAnimation = false
        set(value) {
            if (field != value) {
                field = value
            }
        }
    private var mOpenAnim: Animation? = null
    private var mCloseAnim: Animation? = null
    private var mOpenHeight = 0
    private var mCloseHeight = 0
    private var mExpandable = false
    private var mCloseInNewLine = false
        set(value) {
            if (field != value) {
                field = value
                updateCloseSuffixSpan()
            }
        }
    private var mOpenSuffixSpan: SpannableString? = null
    private var mCloseSuffixSpan: SpannableString? = null
    private var mOpenSuffixStr = DEFAULT_OPEN_SUFFIX
        set(value) {
            if (field != value) {
                field = value
                updateOpenSuffixSpan()
            }
        }
    private var mCloseSuffixStr = DEFAULT_CLOSE_SUFFIX
        set(value) {
            if (field != value) {
                field = value
                updateCloseSuffixSpan()
            }
        }
    @ColorInt
    private var mOpenSuffixColor: Int = Color.parseColor("#F23030")
        set(value) {
            if (field != value) {
                field = value
                updateOpenSuffixSpan()
            }
        }
    @ColorInt
    private var mCloseSuffixColor: Int = Color.parseColor("#F23030")
        set(value) {
            if (field != value) {
                field = value
                updateCloseSuffixSpan()
            }
        }

    private var mCharSequenceToSpannableHandler: ((CharSequence) -> SpannableStringBuilder)?= null

    init {
        movementMethod = LinkMovementMethod.getInstance()
        includeFontPadding = false

    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

    fun setOriginalText(originalText: CharSequence) {
        this.originalText = originalText
        mExpandable = false
        mCloseSpannableStr = SpannableStringBuilder()
        var maxLines = mMaxLines
        val tempText = charSequenceToSpannable(originalText)
        mOpenSpannableStr = charSequenceToSpannable(originalText)

        if (maxLines != -1) {
            val textLayout = createLayout(tempText)
            mExpandable = textLayout.lineCount > maxLines
            if (mExpandable) {
                if (mCloseInNewLine) mOpenSpannableStr?.appendln()
                mCloseSuffixSpan?.let {
                    mOpenSpannableStr?.append(it)
                }
                var endPos = textLayout.getLineEnd(maxLines - 1)
                mCloseSpannableStr = if (originalText.length <= endPos) {
                    charSequenceToSpannable(originalText)
                } else {
                    charSequenceToSpannable(originalText.subSequence(0, endPos))
                }

                var tempText2 = charSequenceToSpannable(mCloseSpannableStr!!).append(ELLIPSIS_STRING)
                mOpenSuffixSpan?.let {
                    tempText2.append(it)
                }
                var tempLayout = createLayout(tempText2)
                while (tempLayout.lineCount > maxLines) {
                    val lastSpace = mCloseSpannableStr!!.length - 1
                    if (lastSpace == -1) {
                        break
                    }
                    mCloseSpannableStr = if (originalText.length <= lastSpace) {
                        charSequenceToSpannable(originalText)
                    } else {
                        charSequenceToSpannable(originalText.subSequence(0, lastSpace))
                    }
                    tempText2 = charSequenceToSpannable(mCloseSpannableStr!!).append(ELLIPSIS_STRING)
                    mOpenSuffixSpan?.let {
                        tempText2.append(it)
                    }
                    tempLayout = createLayout(tempText2)
                }
                val lastSpace = mCloseSpannableStr!!.length - mOpenSuffixSpan!!.length
                if (lastSpace >= 0 && originalText.length > lastSpace) {
                    mCloseSpannableStr = charSequenceToSpannable(originalText.subSequence(0, lastSpace))

                }
                mCloseHeight = tempLayout.height + paddingTop + paddingBottom
                mOpenSuffixSpan?.let {
                    mCloseSpannableStr?.append(it)
                }
            }
        }
        isClosed = mExpandable
        text = if (mExpandable) {
            mCloseSpannableStr
        } else {
            mOpenSpannableStr
        }
    }

    private fun charSequenceToSpannable(charSequence: CharSequence): SpannableStringBuilder {
        return mCharSequenceToSpannableHandler?.invoke(charSequence) ?: SpannableStringBuilder(charSequence)
    }

    private fun createLayout(spannable: SpannableStringBuilder): StaticLayout {
        val contentWidth = initWidth - paddingStart - paddingEnd
        return newStaticLayout(
            spannable, paint, contentWidth,
            Layout.Alignment.ALIGN_NORMAL, includeFontPadding
        )
    }

    private fun switchOpenClose() {
        if (mExpandable) {
            isClosed = !isClosed
            if (isClosed) {
                close()
            } else {
                open()
            }
        }
    }

    private fun open() {

    }

    private fun close() {

    }

    fun initWidth(width: Int) {
        initWidth = width
    }

    override fun setMaxLines(maxLines: Int) {
        this.mMaxLines = maxLines
        super.setMaxLines(maxLines)
    }

    private fun updateOpenSuffixSpan() {

    }

    private fun updateCloseSuffixSpan() {

    }
}