package com.luuu.seven.util.observer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.luuu.seven.util.getLifecycle

class BatteryObserver(
    private val context: Context,
    private val lifecycle: Lifecycle,
    listener: (info: BatteryInfo) -> Unit
) : DefaultLifecycleObserver {

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (Intent.ACTION_BATTERY_CHANGED == intent?.action) {
                val level = intent.getIntExtra("level", 0)
                val scale = intent.getIntExtra("scale", 100)
                val status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)
                listener.invoke(
                    BatteryInfo(
                        level,
                        scale,
                        status
                    )
                )
            }
        }
    }

    init {
        //为什么用context获取的lifecycle和传递进来的不一样？？？？
        lifecycle.addObserver(this)
        context.registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onDestroy(owner: LifecycleOwner) {
        context.unregisterReceiver(batteryReceiver)
        lifecycle.removeObserver(this)
    }
}

data class BatteryInfo(
    val level: Int,
    val scale: Int,
    val status: Int
) {
    val power: Int
        get() = level * 100 / scale

    val batteryCharging: Boolean
        get() = status == BatteryManager.BATTERY_STATUS_CHARGING
}