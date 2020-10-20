package com.luuu.seven.util.observer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.luuu.seven.util.getLifecycle

class BatteryObserver(
    private val context: Context,
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

    private val life = context.getLifecycle() ?: GlobalLifecycle

    init {
        life.addObserver(this)
        context.registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onDestroy(owner: LifecycleOwner) {
        context.unregisterReceiver(batteryReceiver)
        life.removeObserver(this)
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