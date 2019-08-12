package com.luuu.seven.util

class ComicLaunch<T> {

    internal var onSuccess: ((T) -> Unit)? = null
    internal var onComplete: (() -> Unit)? = null
    internal var onFailed: ((error: String, code: Int) -> Unit)? = null

    internal lateinit var request: (suspend () -> T)
    fun request(block: (suspend () -> T)) {
        this.request = block
    }

    fun onSuccess(block: (T) -> Unit) {
        this.onSuccess = block
    }

    fun onComplete(block: () -> Unit) {
        this.onComplete = block
    }

    fun onFailed(block: (error: String, code: Int) -> Unit) {
        this.onFailed = block
    }

}