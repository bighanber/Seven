package com.luuu.seven.http

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
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
        val getInstance: HttpManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpManager() }
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

    private class LogInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
            Log.e("RetrofitManager", "request:" + request.toString())
            val t1 = System.nanoTime()
            val response = chain.proceed(chain.request())
            val t2 = System.nanoTime()
            Log.e("RetrofitManager", String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6, response.headers()))
            val mediaType = response.body()?.contentType()
            val content = response.body()?.string()
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build()
        }
    }
}