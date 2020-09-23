package com.luuu.seven.util.bracer

import com.google.gson.Gson

internal val gson by lazy { Gson() }

internal inline fun <P, reified R> P?.or(defaultValue: () -> R): R {
    return this as? R ?: defaultValue()
}