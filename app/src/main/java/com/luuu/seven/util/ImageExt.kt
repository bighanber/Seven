package com.luuu.seven.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.luuu.seven.http.Api
import com.luuu.seven.util.img.GlideStrategy
import com.luuu.seven.util.img.ImgLoad

fun ImageView.loadWithHead(url: String, head: String = Api.DMZJ) {
//    get(GlideUrl(url, LazyHeaders.Builder().addHeader("Referer", head).build())).into(this)
    ImgLoad.instance.with(context).setLoadType(url).setHeader("Referer", head).build(GlideStrategy()).into(this)
}

fun url2Bitmap(context: Context, url: String): Bitmap {
    return ImgLoad.instance.with(context).setLoadType(url).build(GlideStrategy()).url2Bitmap()
}