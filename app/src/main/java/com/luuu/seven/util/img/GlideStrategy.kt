package com.luuu.seven.util.img

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

class GlideStrategy : ImgLoadStrategy {

    override fun getBitmap(config: ImgLoadBuilder): Bitmap {
        return Glide.with(config.context).asBitmap().load(config.url).submit().get()
    }

    override fun loadImg(config: ImgLoadBuilder) {

        val mRequestBuilder = when {
            config.url != null -> {
                if (!config.headerKey.isNullOrEmpty() && !config.headerValue.isNullOrEmpty()) {
                    Glide.with(config.context).load(
                        GlideUrl(
                            config.url, LazyHeaders.Builder().addHeader(
                                config.headerKey!!, config.headerValue!!
                            ).build()
                        )
                    )
                } else {
                    Glide.with(config.context).load(config.url)
                }
            }
            config.bitmap != null -> Glide.with(config.context).load(config.bitmap)
            config.uri != null -> Glide.with(config.context).load(config.uri)
            config.drawableId != null -> Glide.with(config.context).load(config.drawableId)
            config.file != null -> Glide.with(config.context).load(config.file)
            else -> throw NullPointerException("Please set image address first")
        }

        config.placeholderId?.let {
            mRequestBuilder.placeholder(it)
        }

        config.placeholderDrawable?.let {
            mRequestBuilder.placeholder(it)
        }

        config.errorId?.let {
            mRequestBuilder.error(it)
        }

        config.errorPlaceholder?.let {
            mRequestBuilder.error(it)
        }

        if (config.skipMemoryCache) mRequestBuilder.skipMemoryCache(true)
        if (config.isCenterCrop) mRequestBuilder.centerCrop()
        if (config.isCenterInside) mRequestBuilder.centerInside()
        if (config.isFitCenter) mRequestBuilder.fitCenter()
        if (config.isCircleCrop) mRequestBuilder.circleCrop()

        config.targetView?.let {
            mRequestBuilder.into(it)
        }
    }

    override fun clearMemoryCache() {
    }

    override fun clearDiskCache() {
    }

}