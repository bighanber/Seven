package com.luuu.seven.http.interceptor

import android.util.Log
import okhttp3.Interceptor
import java.io.IOException
import java.util.*

class LogInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        Log.e("RetrofitManager", "request: $request")
        val t1 = System.nanoTime()
        val response = chain.proceed(chain.request())
        val t2 = System.nanoTime()
        Log.e("RetrofitManager", String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6, response.headers()))
        Log.e("RetrofitManager", response.peekBody(1024 * 1024).string())
        val mediaType = response.body()?.contentType()
        val content = response.body()?.string()
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build()
    }
}