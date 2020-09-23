package com.luuu.seven.util.img

import android.graphics.Bitmap

interface ImgLoadStrategy {
    fun loadImg(config: ImgLoadBuilder)
    fun getBitmap(config: ImgLoadBuilder): Bitmap
    fun clearMemoryCache()
    fun clearDiskCache()
}