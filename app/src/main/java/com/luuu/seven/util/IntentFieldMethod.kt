package com.luuu.seven.util

import android.content.Intent
import android.os.BaseBundle
import android.os.Build
import android.os.Bundle
import java.lang.reflect.Field
import java.lang.reflect.Method

internal object IntentFieldMethod {
    lateinit var mExtras: Field
    lateinit var mMap: Field
    lateinit var unparcel: Method

    init {
        try {
            mExtras = Intent::class.java.getDeclaredField("mExtras")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMap = BaseBundle::class.java.getDeclaredField("mMap")
                unparcel = BaseBundle::class.java.getDeclaredMethod("unparcel")
            } else {
                mMap = Bundle::class.java.getDeclaredField("mMap")
                unparcel = Bundle::class.java.getDeclaredMethod("unparcel")
            }
            mExtras.isAccessible = true
            mMap.isAccessible = true
            unparcel.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}