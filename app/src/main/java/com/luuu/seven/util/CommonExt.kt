package com.luuu.seven.util

import android.content.Context
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.os.ParcelCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.luuu.seven.ComicApplication

fun Context.dp2px(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dp2px(value: Float): Int = (value * resources.displayMetrics.density).toInt()
fun Context.sp2px(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.sp2px(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.px2dp(px: Int): Float = px.toFloat() / resources.displayMetrics.density
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity
fun Context.dimen2px(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)

fun Context.getVersionName(): String = packageManager.getPackageInfo(packageName, 0).versionName
fun Context.getVersionCode(): Int = packageManager.getPackageInfo(packageName, 0).versionCode
fun Context.getAppName(): String =
    resources.getString(packageManager.getPackageInfo(packageName, 0).applicationInfo.labelRes)

fun Context.inflateLayout(@LayoutRes layoutId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View =
    LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)

val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

fun View.dp2px(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun View.dp2px(value: Float): Int = (value * resources.displayMetrics.density).toInt()
fun View.sp2px(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun View.sp2px(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun View.px2dp(px: Int): Float = px.toFloat() / resources.displayMetrics.density
fun View.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity
fun View.dimen2px(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)

fun toast(msg: String? = "error", duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(ComicApplication.mApp, msg, duration).show()
}

fun toast(@StringRes msg: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(ComicApplication.mApp, ComicApplication.mApp.resources.getString(msg), duration).show()
}

fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T) {
    if (this.value != newValue) value = newValue
}

fun Parcel.writeBooleanUsingCompat(value: Boolean) = ParcelCompat.writeBoolean(this, value)

fun Parcel.readBooleanUsingCompat() = ParcelCompat.readBoolean(this)

