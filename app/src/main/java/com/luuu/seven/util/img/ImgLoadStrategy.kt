package com.luuu.seven.util.img

interface ImgLoadStrategy {
    fun loadImg(config: ImgLoadBuilder)
    fun clearMemoryCache()
    fun clearDiskCache()
}