package com.luuu.seven.http

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.luuu.seven.http.interceptor.LogManager
import com.luuu.seven.http.interceptor.LogProxy
import com.luuu.seven.http.interceptor.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   :
 *     version:
 */
class HttpManager private constructor() {

    companion object {
        val getInstance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = HttpManager()
    }

    private val httpLoggingInterceptor by lazy {
        LoggingInterceptor.Builder()
            .loggable(true)
            .request()
            .requestTag("Request")
            .response()
            .responseTag("Response")
            .build()
    }

    private var mRetrofit: Retrofit? = null
    private var mNetService: DataService? = null

    init {
        if (mRetrofit == null) createRetrofit()
    }

    fun getService(): DataService = mNetService!!

    private fun createRetrofit() {
        logInit()

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
//          .addInterceptor(HeadInterceptor())
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()


        mRetrofit = Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Api.DMZJ_URL)
            .build()
        mNetService = mRetrofit?.create(DataService::class.java)
    }

    private fun logInit() {
        LogManager.logProxy(object : LogProxy {
            override fun e(tag: String, msg: String) {
                Log.e(tag,msg)
            }

            override fun w(tag: String, msg: String) {
                Log.w(tag,msg)
            }

            override fun i(tag: String, msg: String) {
                Log.i(tag,msg)
            }

            override fun d(tag: String, msg: String) {
                Log.d(tag,msg)
            }
        })
    }
}