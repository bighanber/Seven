package com.luuu.seven.http.interceptor

interface LogProxy {

    fun e(tag:String , msg:String)

    fun w(tag:String , msg:String)

    fun i(tag:String , msg:String)

    fun d(tag:String , msg:String)
}