package com.luuu.seven.util

import android.os.Build
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.*

/**
 * Created by lls on 2017/11/9.
 *
 */

//fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
//    Toast.makeText(context, message, duration).show()
//}
//
//fun Fragment.showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
//    Snackbar.make(view, message, duration).show()
//}

/**
 * @param source
 * @param paint
 * @param width 文字区域的宽度
 * @param alignment 文字的对齐方向
 * @param spacingmult 行间距的倍数
 * @param spacingadd 行间距的额外增加值
 * @param includepad 文字上下添加额外的空间
 */
fun newStaticLayout(
    source: CharSequence,
    paint: TextPaint,
    width: Int,
    alignment: Layout.Alignment,
    spacingmult: Float,
    spacingadd: Float,
    includepad: Boolean
): StaticLayout {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        StaticLayout.Builder.obtain(source, 0, source.length, paint, width).apply {
            setAlignment(alignment)
            setLineSpacing(spacingadd, spacingmult)
            setIncludePad(includepad)
        }.build()
    } else {
        @Suppress("DEPRECATION")
        StaticLayout(source, paint, width, alignment, spacingmult, spacingadd, includepad)
    }
}

fun StaticLayout.textWidth(): Int {
    var width = 0f
    for (i in 0 until lineCount) {
        width = width.coerceAtLeast(getLineWidth(i))
    }
    return width.toInt()
}

fun lerp(a: Float, b: Float, t: Float): Float {
    return a + (b - a) * t
}

fun isSDCardEnable() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

fun Float.normalize(
    inputMin: Float,
    inputMax: Float,
    outputMin: Float,
    outputMax: Float
): Float {
    if (this < inputMin) {
        return outputMin
    } else if (this > inputMax) {
        return outputMax
    }

    return outputMin * (1 - (this - inputMin) / (inputMax - inputMin)) +
            outputMax * ((this - inputMin) / (inputMax - inputMin))
}

fun Int.centsToDollars(): Double = this.toDouble() / 100.0

fun Int.centsToDollarsFormat(currency: String): String {
    val dollars = this / 100
    val cents = this % 100
    return String.format(Locale.getDefault(), "%s%d.%02d", currency, dollars, cents)
}

fun Double.toPrice(): String {
    val pattern = "#,###.00"
    val decimalFormat = DecimalFormat(pattern)
    decimalFormat.groupingSize = 3

    return "¥" + decimalFormat.format(this)
}

val <T> T.weak: WeakReference<T>
    get() = WeakReference(this)