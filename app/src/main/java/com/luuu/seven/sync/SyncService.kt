package com.luuu.seven.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SyncService : Service() {

    companion object {
        private var sSyncAdapter: SyncAdapter? = null
        private val sSyncAdapterLock = Any()
    }

    override fun onCreate() {
        synchronized(sSyncAdapterLock) {
            sSyncAdapter = sSyncAdapter ?: SyncAdapter(applicationContext, true)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return sSyncAdapter?.syncAdapterBinder ?: throw IllegalAccessException()
    }

}