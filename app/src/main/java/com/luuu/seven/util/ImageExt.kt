package com.luuu.seven.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.Coil
import coil.load
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.BlurTransformation
import com.luuu.seven.http.Api

fun ImageView.loadWithHead(url: String, head: String = Api.DMZJ) {
//    get(GlideUrl(url, LazyHeaders.Builder().addHeader("Referer", head).build())).into(this)
//    ImgLoad.instance.with(context).setLoadType(url).setHeader("Referer", head).build(GlideStrategy()).into(this)

    load(url) {
        addHeader("Referer", head)
        crossfade(true)
    }
}

fun ImageView.loadBlur(url: String, head: String = Api.DMZJ) {

    load(url) {
        addHeader("Referer", head)
        transformations(BlurTransformation(context))
    }
}

suspend fun url2Bitmap(context: Context, url: String): Bitmap? {
//    return ImgLoad.instance.with(context).setLoadType(url).build(GlideStrategy()).url2Bitmap()
    val imageLoader = Coil.imageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(url)
        .allowHardware(false)
        .build()
    return imageLoader.execute(request).drawable?.toBitmap()
}

fun ImageView.loadImgWithProgress(imageUrl: String, view: ProgressBar) {
    val imageLoader = Coil.imageLoader(context)
    val request = ImageRequest.Builder(context).data(imageUrl).target(
        onStart = {
            view.show()
        },
        onSuccess = {
            view.gone()
        },
        onError = {
            view.gone()
        }
    ).build()
    imageLoader.enqueue(request)
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