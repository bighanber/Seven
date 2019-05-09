package com.luuu.seven.http

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.luuu.seven.http.interceptor.HeadInterceptor
import com.luuu.seven.http.interceptor.LogInterceptor
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
        val holder= HttpManager()
    }


    private var mRetrofit: Retrofit? = null
    private var mNetService: DataService? = null

    init {
        if (mRetrofit == null) createRetrofit()
    }

    fun getService(): DataService = mNetService!!

    private fun createRetrofit() {
        val client = OkHttpClient.Builder()
                .addInterceptor(LogInterceptor())
//                .addInterceptor(HeadInterceptor())
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

}