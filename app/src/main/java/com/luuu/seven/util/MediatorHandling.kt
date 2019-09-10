package com.luuu.seven.util

import androidx.lifecycle.MediatorLiveData

abstract class MediatorHandling<in P, R> {
    protected val result = MediatorLiveData<Result<R>>()

    open fun observe(): MediatorLiveData<Result<R>> {
        return result
    }

    abstract fun execute(parameters: P)
}