package com.luuu.seven.util

import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.luuu.seven.ComicApplication

/**
 * Created by lls on 2017/11/9.
 *
 */
fun ImageView.loadImg(imageUrl: String) {
//    if (TextUtils.isEmpty(imageUrl)) {
//        Glide.with(ComicApplication.sAppContext).load(R.mipmap.ic_launcher).into(this)
//    } else {
//        Glide.with(ComicApplication.sAppContext).load(GlideUrl(imageUrl, LazyHeaders.Builder().addHeader("Referer", Api.DMZJ).build()))
//                .into(this)
//    }
}

fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    } else {
        Log.e("IfNotNull", "some value is null")
    }
}

fun ImageView.loadImgWithTransform(imageUrl: String) {
//    Glide.with(ComicApplication.sAppContext).load(GlideUrl(imageUrl, LazyHeaders.Builder().addHeader("Referer", Api.DMZJ).build()))
//            .bitmapTransform(BlurTransformation(context, 50))
//            .into(object : SimpleTarget<GlideDrawable>() {
//                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
//                    background = resource
//                }
//            })
}

fun ImageView.loadImgWithProgress(imageUrl: String, view: ProgressBar) {
//    Glide.with(ComicApplication.sAppContext)
//            .load(GlideUrl(imageUrl, LazyHeaders.Builder().addHeader("Referer", Api.DMZJ).build()))
//            .listener(object : RequestListener<GlideUrl, GlideDrawable> {
//                override fun onResourceReady(resource: GlideDrawable?, model: GlideUrl?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
//                    view.visibility = View.INVISIBLE
//                    return false
//                }
//
//                override fun onException(e: Exception?, model: GlideUrl?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
//                    return false
//                }
//
//            })
//            .into(this)
}
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
    includepad: Boolean,
    spacingmult: Float = 1f,
    spacingadd: Float = 0f
): StaticLayout {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        StaticLayout.Builder.obtain(source, 0, source.length, paint, width).apply {
            setAlignment(alignment)
            setLineSpacing(spacingadd, spacingmult)
            setIncludePad(includepad)
        }.build()
    } else {
        @Suppress("DEPRECATION")
        (StaticLayout(source, paint, width, alignment, spacingmult, spacingadd, includepad))
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