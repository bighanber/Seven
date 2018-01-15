package com.luuu.seven.util

import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.luuu.seven.R
import com.luuu.seven.http.Api

/**
 * Created by lls on 2017/11/9.
 *
 */
fun ImageView.loadImg(imageUrl: String) {
    if (TextUtils.isEmpty(imageUrl)) {
        Glide.with(context).load(R.mipmap.ic_launcher).into(this)
    } else {
        Glide.with(context).load(GlideUrl(imageUrl, LazyHeaders.Builder().addHeader("Referer", Api.DMZJ).build()))
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

//fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
//    Toast.makeText(context, message, duration).show()
//}
//
//fun Fragment.showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
//    Snackbar.make(view, message, duration).show()
//}