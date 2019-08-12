package com.luuu.seven.util

import android.content.Context
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.string(@StringRes id: Int): String = getString(id)
fun Fragment.string(@StringRes id: Int, vararg str: String?): String = getString(id, *str)
fun Fragment.stringArr(@ArrayRes id: Int): Array<String> = resources.getStringArray(id)
fun Fragment.color(@ColorRes id: Int): Int = ContextCompat.getColor(context!!, id)

fun Context.string(@StringRes id: Int): String = getString(id)
fun Context.string(@StringRes id: Int, vararg str: String?): String = getString(id, *str)
fun Context.stringArr(@ArrayRes id: Int): Array<String> = resources.getStringArray(id)
fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun View.string(@StringRes id: Int): String = context.string(id)
fun View.string(@StringRes id: Int, vararg str: String?): String = context.string(id, *str)
fun View.stringArr(@ArrayRes id: Int): Array<String> = context.stringArr(id)
fun View.color(@ColorRes id: Int): Int = context.color(id)