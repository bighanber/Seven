package com.luuu.seven.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.load
import coil.request.ImageRequest
import coil.transform.BlurTransformation
import com.luuu.seven.http.Api

fun ImageView.loadWithHead(url: String, head: String = Api.DMZJ) {
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
}