package com.luuu.seven

import android.os.Bundle
import com.luuu.seven.base.BaseActivity

class MainActivity : BaseActivity() {

    companion object {
//        const val AUTHORITY = "com.example.android.datasync.provider"
//        const val ACCOUNT_TYPE = "example.com"
//        const val ACCOUNT = "dummyaccount"
    }

//    private lateinit var mAccount: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
    }

    override fun initViews() {
//        updateForTheme(ThemedActivityDelegateImpl().currentTheme)
//        ThemedActivityDelegateImpl().theme.observe(this, Observer(::updateForTheme))

//        mAccount = createSyncAccount()
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_main

//    private fun createSyncAccount(): Account {
//        val accountManager = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
//        return Account(ACCOUNT, ACCOUNT_TYPE).also { newAccount ->
//            if (accountManager.addAccountExplicitly(newAccount, null, null)) {
//
//            } else {
//
//            }
//        }
//    }
}
