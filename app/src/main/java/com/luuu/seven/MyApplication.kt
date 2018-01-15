package com.luuu.seven

import android.app.Application
import kotlin.properties.Delegates

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/07/31
 *     desc   :
 *     version:
 */
class MyApplication : Application() {

    companion object {
        var sAppContext: MyApplication by Delegates.notNull()
        lateinit var cacheDir: String
    }

    init {
        sAppContext = this
    }

    override fun onCreate() {
        super.onCreate()
        if (applicationContext.externalCacheDir != null && ExistSDCard()) {
            MyApplication.cacheDir = applicationContext.externalCacheDir.toString()

        } else {
            MyApplication.cacheDir = applicationContext.cacheDir.toString()
        }
    }

    fun ExistSDCard(): Boolean {

        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)
    }
}