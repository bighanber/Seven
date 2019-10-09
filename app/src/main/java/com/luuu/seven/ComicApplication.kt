package com.luuu.seven

import android.app.Application
import com.luuu.seven.util.isSDCardEnable
import kotlin.properties.Delegates

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/07/31
 *     desc   :
 *     version:
 */
class ComicApplication : Application() {

    companion object {
        var mApp: ComicApplication by Delegates.notNull()
        lateinit var cacheDir: String
    }

    init {
        mApp = this
    }

    override fun onCreate() {
        super.onCreate()
//        applicationContext.externalMediaDirs  该目录下的文件能够被其他应用访问和被MediaStore查询和获取
        if (applicationContext.externalCacheDir != null && isSDCardEnable()) {
            ComicApplication.cacheDir = applicationContext.externalCacheDir.toString()
        } else {
            ComicApplication.cacheDir = applicationContext.cacheDir.toString()
        }
    }

}