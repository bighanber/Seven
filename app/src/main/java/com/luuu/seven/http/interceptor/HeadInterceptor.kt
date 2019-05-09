package com.luuu.seven.http.interceptor

import com.luuu.seven.http.Api
import okhttp3.Interceptor
import okhttp3.Response

class HeadInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
                .addHeader("Referer", Api.REFERER)
                .addHeader("User-Agent", Api.AGENT)

        val request = builder.build()
        return chain.proceed(request)
    }

}