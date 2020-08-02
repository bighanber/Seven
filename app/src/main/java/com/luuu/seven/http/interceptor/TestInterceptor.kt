package com.luuu.seven.http.interceptor

import android.util.Log
import okhttp3.*
import okio.Buffer
import java.io.IOException


class TestInterceptor : Interceptor {

    private val newHost = "127.0.0.1"
    private val path1 = "/test/upload/img"
    private val path2 = "/test/upload/voice"
    private val TAG = "TestInterceptor"

    @Throws(IOException::class)
    fun requestBodyToString(requestBody: RequestBody): String? {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readUtf8()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)

        val url: HttpUrl = request.url
        //http://127.0.0.1/test/upload/img?userName=xiaoming&userPassword=12345
        //http://127.0.0.1/test/upload/img?userName=xiaoming&userPassword=12345
        val scheme = url.scheme //  http https
        val host = url.host //   127.0.0.1
        val path = url.encodedPath //  /test/upload/img
        val query = url.encodedQuery //  userName=xiaoming&userPassword=12345

        val sb = StringBuffer()
        val newUrl =
            sb.append(scheme).append(newHost).append(path).append("?").append(query).toString()

        val builder = request.newBuilder()
            .url(newUrl)

        //URL重定向
//        return chain.proceed(builder.build())

        val sbb = StringBuffer()
        sbb.append(scheme).append(newHost).append(path).append("?")
        val queryList = url.queryParameterNames
        val iterator: Iterator<String> = queryList.iterator()

        for (i in queryList.indices) {
            val queryName = iterator.next()
            sbb.append(queryName).append("=")
            val queryKey = url.queryParameter(queryName)
            //对query的key进行加密
//            sbb.append(CommonUtils.getMD5(queryKey))
            if (iterator.hasNext()) {
                sbb.append("&")
            }
        }

        //加密query内容
//        val newUrl = sb.toString()
//
//        val builder = request.newBuilder()
//            .url(newUrl)
//
//        return chain.proceed(builder.build())

        //--------------------

        //加密body体内容
//        val body1 = request.body()
//        val bodyToString1 = requestBodyToString(body1!!)
//        val testBean: TestBean = GsonTools.changeGsonToBean(bodyToString, TestBean::class.java)
//        val userPassword: String = testBean.getUserPassword()
        //加密body体中的用户密码
        //加密body体中的用户密码
//        testBean.setUserPassword(CommonUtils.getMD5(userPassword))

//        val testGsonString: String = GsonTools.createGsonString(testBean)
//        val requestBody =
//            RequestBody.create(MediaType.parse("application/json"), testGsonString)
//        val builder = request.newBuilder()
//            .post(requestBody)
//            .url(newUrl)

//        return chain.proceed(builder.build())
        //--------------------

        //HEAD动态添加
//        when (path) {
//            path1 -> builder.addHeader("token", "token")
//            path2 -> {
//                builder.addHeader("token", "token")
//                builder.addHeader("uid", "uid")
//            }
//        }
//        return chain.proceed(builder.build())
        //--------------------
        //请求日志抓取

        val body = request.body
        val bodyToString = requestBodyToString(body!!)

        Log.e(TAG, scheme)
        Log.e(TAG, host)
        Log.e(TAG, path)
        Log.e(TAG, query)

        if (response != null) {
            val responseBody = response.body
            val contentLength = responseBody!!.contentLength()
            val bodySize =
                if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
            Log.e(
                TAG, response.code.toString() + ' ' + response.message + ' '
                        + response.request.url + ' '
                        + bodySize
            )
            val headers = response.headers
            var i = 0
            val count = headers.size
            while (i < count) {
                Log.e(TAG, headers.name(i).toString() + ": " + headers.value(i))
                i++
            }
        }
        return chain.proceed(request)
    }

}