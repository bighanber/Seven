package com.luuu.seven.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.luuu.seven.SplashActivity

/**
 * 拨号盘*#*#77#*#*启动应用
 */
class SecretCodeReceiver : BroadcastReceiver() {

    companion object {
        private const val SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && SECRET_CODE_ACTION == intent.action) {
            val it = Intent(context, SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context?.startActivity(it)
        }
    }

}