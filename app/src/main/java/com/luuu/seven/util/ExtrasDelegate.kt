package com.luuu.seven.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

class ExtrasDelegate<T> (private val name: String, private val defaultValue: T) {

    private var extra: T? = null

    operator fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        extra = getExtra(extra, name, thisRef)
        return extra ?: defaultValue
    }

    operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        extra = getExtra(extra, name, thisRef)
        return extra ?: defaultValue
    }

    fun <T> extraDelegate(extra: String, default: T) = ExtrasDelegate(extra, default)

    fun extraDelegate(extra: String) = extraDelegate(extra, null)

    private fun <T> getExtra(oldExtra: T?, extraName: String, thisRef: AppCompatActivity): T? =
        oldExtra ?: thisRef.intent?.extras?.get(extraName) as T?

    private fun <T> getExtra(oldExtra: T?, extraName: String, thisRef: Fragment): T? =
        oldExtra ?: thisRef.arguments?.get(extraName) as T?

}

