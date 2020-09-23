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
class SevenImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        imageView?.loadWithHead(path as String)
    }
}