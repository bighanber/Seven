package com.luuu.seven.util

import android.os.Build
import java.util.*

fun isHuawei(): Boolean {
    return if (Build.BRAND == null) {
        false
    } else {
        Build.BRAND.toLowerCase(Locale.ROOT) == "huawei" || Build.BRAND.toLowerCase(Locale.ROOT) == "honor"
    }
}

fun isXiaomi(): Boolean {
    return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "xiaomi"
}

fun isOPPO(): Boolean {
    return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "oppo"
}

fun isVIVO(): Boolean {
    return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "vivo"
}

fun isMeizu(): Boolean {
    return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "meizu"
}

fun isSamsung(): Boolean {
    return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "samsung"
}

fun isSmartisan(): Boolean {
    return Build.BRAND != null && Build.BRAND.toLowerCase(Locale.ROOT) == "smartisan"
}
