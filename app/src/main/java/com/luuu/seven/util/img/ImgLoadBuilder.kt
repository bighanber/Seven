package com.luuu.seven.util.img

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import java.io.File


class ImgLoadBuilder(strategy: ImgLoadStrategy, builder: Builder) {

    var strategy: ImgLoadStrategy = strategy
    var context: Context = builder.context
    var placeholderId: Int? = builder.placeholderId
    var placeholderDrawable: Drawable? = builder.placeholderDrawable
    var errorId: Int? = builder.errorId
    var errorPlaceholder: Drawable? = builder.errorPlaceholder
    var isCenterCrop: Boolean = builder.isCenterCrop
    var isFitCenter: Boolean = builder.isFitCenter
    var isCenterInside: Boolean = builder.isCenterInside
    var isCircleCrop: Boolean = builder.isCircleCrop
    var skipMemoryCache: Boolean = builder.skipMemoryCache
    var bitmapConfig = builder.bitmapConfig
    var targetWidth: Int? = builder.targetWidth
    var targetHeight: Int? = builder.targetHeight
    var url: String? = builder.url
    var file: File? = builder.file
    var drawableId: Int? = builder.drawableId
    var uri: Uri? = builder.uri
    var bitmap: Bitmap? = builder.bitmap
    var headerKey: String? = builder.headerKey
    var headerValue: String? = builder.headerValue
    var targetView: ImageView? = null

    fun into(targetView: ImageView) {
        this.targetView = targetView
        this.strategy.loadImg(this)
    }

    class Builder private constructor() {

        constructor(context: Context): this() {
            this.context = context
        }

        lateinit var context: Context
        var placeholderId: Int? = null
        var placeholderDrawable: Drawable? = null
        var errorId: Int? = null
        var errorPlaceholder: Drawable? = null
        var isCenterCrop: Boolean = false
        var isFitCenter: Boolean = false
        var isCenterInside: Boolean = false
        var isCircleCrop: Boolean = false
        var skipMemoryCache: Boolean = false
        var bitmapConfig = Bitmap.Config.RGB_565
        var targetWidth: Int? = null
        var targetHeight: Int? = null
        var url: String? = null
        var file: File? = null
        var drawableId: Int? = null
        var uri: Uri? = null
        var bitmap: Bitmap? = null
        var headerKey: String? = null
        var headerValue: String? = null


        fun setPlaceholder(@DrawableRes resId: Int): Builder {
            placeholderId = resId
            return this
        }

        fun setPlaceholder(drawable: Drawable): Builder {
            placeholderDrawable = drawable
            return this
        }

        fun setError(@DrawableRes resId: Int): Builder {
            errorId = resId
            return this
        }

        fun setError(drawable: Drawable): Builder {
            errorPlaceholder = drawable
            return this
        }

        fun setCenterCrop(centerCrop: Boolean): Builder {
            isCenterCrop = centerCrop
            return this
        }

        fun setFitCenter(fitCenter: Boolean): Builder {
            isFitCenter = fitCenter
            return this
        }

        fun setCenterInside(centerInside: Boolean): Builder {
            isCenterInside = centerInside
            return this
        }

        fun setCircleCrop(circleCrop: Boolean): Builder {
            isCircleCrop = circleCrop
            return this
        }

        fun setSkipMemoryCache(skip: Boolean): Builder {
            skipMemoryCache = skip
            return this
        }

        fun setBitmapConfig(config: Bitmap.Config): Builder {
            bitmapConfig = config
            return this
        }

        fun setOverride(width: Int, height: Int): Builder {
            targetWidth = width
            targetHeight = height
            return this
        }

        fun setLoadType(url: String): Builder {
            this.url = url
            return this
        }

        fun setLoadType(file: File): Builder {
            this.file = file
            return this
        }

        fun setLoadType(drawableId: Int): Builder {
            this.drawableId = drawableId
            return this
        }

        fun setLoadType(uri: Uri): Builder {
            this.uri = uri
            return this
        }

        fun setLoadType(bitmap: Bitmap): Builder {
            this.bitmap = bitmap
            return this
        }

        fun setHeader(headerKey: String, headerValue: String): Builder {
            this.headerKey = headerKey
            this.headerValue = headerValue
            return this
        }

        fun build(imgLoadStrategy: ImgLoadStrategy): ImgLoadBuilder {
            return ImgLoadBuilder(imgLoadStrategy, this)
        }
    }

}