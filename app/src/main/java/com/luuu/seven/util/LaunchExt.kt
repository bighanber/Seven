package com.luuu.seven.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.launch

fun <T> ViewModel.launch(init: ComicLaunch<T>.() -> Unit) {
    val mLaunch = ComicLaunch<T>().apply(init)


    viewModelScope.launch {

        try {
            val result = withContext(Dispatchers.IO) {
                mLaunch.request()
            }
            mLaunch.onSuccess?.invoke(result)
        } catch (e: Exception) {
            when (e) {
                is UninitializedPropertyAccessException -> mLaunch.onFailed?.invoke("request不能为空", 0)
                else -> mLaunch.onFailed?.invoke(e.message.toString(), 0)
            }
        } finally {
            mLaunch.onComplete?.invoke()
        }
    }
}