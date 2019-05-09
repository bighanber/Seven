package com.luuu.seven.util

import android.annotation.TargetApi
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.luuu.seven.MyApplication
import com.luuu.seven.R
import com.luuu.seven.http.Api
import jp.wasabeef.glide.transformations.BlurTransformation
import java.lang.Exception

/**
 * Created by lls on 2017/11/9.
 *
 */
fun ImageView.loadImg(imageUrl: String) {
    if (TextUtils.isEmpty(imageUrl)) {
        Glide.with(MyApplication.sAppContext).load(R.mipmap.ic_launcher).into(this)
    } else {
        Glide.with(MyApplication.sAppContext).load(GlideUrl(imageUrl, LazyHeaders.Builder().addHeader("Referer", Api.DMZJ).build()))
                .into(this)
    }
}

fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    } else {
        Log.e("IfNotNull", "some value is null")
    }
}

fun ImageView.loadImgWithTransform(imageUrl: String) {
    Glide.with(MyApplication.sAppContext).load(GlideUrl(imageUrl, LazyHeaders.Builder().addHeader("Referer", Api.DMZJ).build()))
            .bitmapTransform(BlurTransformation(context, 50))
            .into(object : SimpleTarget<GlideDrawable>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
                    background = resource
                }
            })
}

fun ImageView.loadImgWithProgress(imageUrl: String, view: ProgressBar) {
    Glide.with(MyApplication.sAppContext)
            .load(GlideUrl(imageUrl, LazyHeaders.Builder().addHeader("Referer", Api.DMZJ).build()))
            .listener(object : RequestListener<GlideUrl, GlideDrawable> {
                override fun onResourceReady(resource: GlideDrawable?, model: GlideUrl?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    view.visibility = View.INVISIBLE
                    return false
                }

                override fun onException(e: Exception?, model: GlideUrl?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }

            })
            .into(this)
}

fun String.toast(isShortToast: Boolean = true) = toast(this, isShortToast)

fun toast(msg: Any?, isShort: Boolean = true) {
    msg?.let {
        Toast.makeText(MyApplication.sAppContext, msg.toString(), if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
    }
}
//fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
//    Toast.makeText(context, message, duration).show()
//}
//
//fun Fragment.showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
//    Snackbar.make(view, message, duration).show()
//}