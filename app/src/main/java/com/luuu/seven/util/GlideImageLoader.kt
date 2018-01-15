package com.luuu.seven.util

import android.content.Context
import android.widget.ImageView
import com.youth.banner.loader.ImageLoader



/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:
 */
class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
//        Glide.with(MyApplication.sAppContext)
//                .load(GlideUrl(path as String, LazyHeaders.Builder().addHeader("Referer", Api.DMZJ).build()))
//                .into(imageView)
        imageView?.loadImg(path as String)
    }
}