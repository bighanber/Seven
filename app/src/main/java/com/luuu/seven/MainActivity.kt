package com.luuu.seven

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.module.index.ComicHomeFragment
import com.luuu.seven.module.news.ComicNewsFragment
import com.luuu.seven.module.shelf.ComicShelfFragment
import com.luuu.seven.theme.Theme
import com.luuu.seven.theme.ThemedActivityDelegateImpl
import com.luuu.seven.util.SharedPreferenceStorage
import com.luuu.seven.util.click
import com.luuu.seven.util.updateForTheme
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    companion object {
//        const val AUTHORITY = "com.example.android.datasync.provider"
//        const val ACCOUNT_TYPE = "example.com"
//        const val ACCOUNT = "dummyaccount"
    }

//    private lateinit var mAccount: Account

    private lateinit var fm: FragmentManager

    private var indexFragment: ComicHomeFragment? = null
    private var shelfFragment: ComicShelfFragment? = null
    private var newsFragment: ComicNewsFragment? = null

    override fun initViews() {
//        updateForTheme(ThemedActivityDelegateImpl().currentTheme)
//        ThemedActivityDelegateImpl().theme.observe(this, Observer(::updateForTheme))

//        mAccount = createSyncAccount()
//
        fm = supportFragmentManager
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_index -> {
                    showFragment(0)
                }
                R.id.navigation_shelf -> {
                    showFragment(1)
                }
                R.id.navigation_news -> {
                    showFragment(2)
                }
            }
            true
        }
        showFragment(0)
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_main

    private fun showFragment(id: Int) {
        val ft = fm.beginTransaction()
        hideAllFragment(ft)

        when (id) {
            0 -> if (indexFragment != null) {
                ft.show(indexFragment!!)
            } else {
                indexFragment = ComicHomeFragment.newInstance()
                ft.add(R.id.content, indexFragment!!)
            }

            1 -> if (shelfFragment != null) {
                ft.show(shelfFragment!!)
            } else {
                shelfFragment = ComicShelfFragment()
                ft.add(R.id.content, shelfFragment!!)
            }

            2 -> if (newsFragment != null) {
                ft.show(newsFragment!!)
            } else {
                newsFragment = ComicNewsFragment()
                ft.add(R.id.content, newsFragment!!)
            }

        }
        ft.commit()
    }

    private fun hideAllFragment(ft: FragmentTransaction) {
        indexFragment?.let { ft.hide(indexFragment!!) }
        shelfFragment?.let { ft.hide(shelfFragment!!) }
        newsFragment?.let { ft.hide(newsFragment!!) }
    }

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

    override fun onDestroy() {
        super.onDestroy()
//        indexFragment?.un
//        shelfFragment = null
//        newsFragment = null
    }
}
