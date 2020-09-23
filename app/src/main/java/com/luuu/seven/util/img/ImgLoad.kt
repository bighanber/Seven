package com.luuu.seven.util.img

import android.content.Context


class ImgLoad {
    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = ImgLoad()
    }

    fun with(context: Context) = ImgLoadBuilder.Builder(context)
}
