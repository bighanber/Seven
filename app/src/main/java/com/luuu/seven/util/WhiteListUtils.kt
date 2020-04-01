package com.luuu.seven.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import java.lang.Exception

/**
 * 判断应用是否在白名单中
 */
@RequiresApi(Build.VERSION_CODES.M)
fun Context.isIgnoringBatteryOptimizations(): Boolean {
    var isIgnoring = false
    val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager
    isIgnoring = powerManager?.isIgnoringBatteryOptimizations(packageName) ?: false
    return isIgnoring
}

/**
 * 申请加入白名单
 */
@RequiresApi(Build.VERSION_CODES.M)
fun Context.requestIgnoreBatteryOptimizations() {
    try {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.data = Uri.parse("package:${packageName}")
        startActivity(intent)
    } catch (e: Exception) {

    }
}

fun Context.goHuaweiSetting() {
    try {
        showActivity(
            "com.huawei.systemmanager",
            "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
        )
    } catch (e: Exception) {
        showActivity(
            "com.huawei.systemmanager",
            "com.huawei.systemmanager.optimize.bootstart.BootStartActivity"
        )
    }
}

fun Context.goXiaomiSetting() {
    try {
        showActivity(
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
    } catch (e: Exception) {

    }
}

fun Context.goOPPOSetting() {
    try {
        showActivity("com.coloros.phonemanager")
    } catch (e: Exception) {
        try {
            showActivity("com.oppo.safe")
        } catch (e2: Exception) {
            try {
                showActivity("com.coloros.oppoguardelf")
            } catch (e3: Exception) {
                showActivity("com.coloros.safecenter")
            }
        }
    }
}

fun Context.goVIVOSetting() {
    try {
        showActivity("com.iqoo.secure")
    } catch (e: Exception) {

    }
}

fun Context.goMeizuSetting() {
    try {
        showActivity("com.meizu.safe")
    } catch (e: Exception) {

    }
}

fun Context.goSamsungSetting() {
    try {
        showActivity("com.samsung.android.sm_cn")
    } catch (e: Exception) {
        showActivity("com.samsung.android.sm")
    }
}

fun Context.goSmartisanSetting() {
    try {
        showActivity("com.smartisanos.security")
    } catch (e: Exception) {

    }
}
