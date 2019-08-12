package com.luuu.seven.util

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