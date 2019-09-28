package com.luuu.seven.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luuu.seven.theme.Theme

/**
 * Created by lls on 2019-05-09
 */

fun FragmentActivity.replaceFragment(fragment: Fragment, fragmentContent: Int) {
    supportFragmentManager.transact {
        replace(fragmentContent, fragment)
    }
}

fun FragmentActivity.addFragment(fragment: Fragment, fragmentContent: Int) {
    supportFragmentManager.transact {
        add(fragmentContent, fragment)
    }
}

fun FragmentActivity.addFragment(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun FragmentActivity.showFragment(fragment: Fragment) {
    supportFragmentManager.transact {
        show(fragment)
    }
}

fun FragmentActivity.hideFragment(fragment: Fragment) {
    supportFragmentManager.transact {
        hide(fragment)
    }
}

fun FragmentActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.transact {
        remove(fragment)
    }
}

fun AppCompatActivity.updateForTheme(theme: Theme) = when(theme) {
    Theme.DARK -> delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    Theme.LIGHT -> delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    Theme.SYSTEM -> delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    Theme.AUTO -> delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}

inline fun <reified T : ViewModel> AppCompatActivity.obtainViewModel() =
    ViewModelProvider(this).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.obtainViewModel() =
    ViewModelProvider(this).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.activityViewModel() =
    ViewModelProvider(requireActivity()).get(T::class.java)

inline fun <reified T> Fragment.startActivity(vararg params: Pair<String, Any>, flag: Int = -1) {
    activity?.startActivity<T>(*params, flag = flag)
}

inline fun <reified T> Context.startActivity(vararg params: Pair<String, Any>, flag: Int = -1) {
    val intent = Intent(this, T::class.java).apply {
        if (-1 != flag) {
            flags = flags
        } else if (this@startActivity !is Activity) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
    intent.putExtras(*params)
    startActivity(intent)
}

inline fun <reified T> FragmentActivity.startActivityForResult(vararg params: Pair<String, Any>, flag: Int = -1, requestCode: Int = -1, crossinline callback: (result: Intent?) -> Unit = {}) {
    val intent = Intent(this, T::class.java).apply {
        if (flag != -1) {
            this.addFlags(flag)
        }
    }
    intent.putExtras(*params)
    val fragment = EmptyFragment().apply {
        init(requestCode, intent) {
            callback.invoke(it)
            this@startActivityForResult.removeFragment(this)
        }
    }
    addFragment(fragment, "ForResult")
}

inline fun <reified T> Fragment.startActivityForResult(vararg params: Pair<String, Any>, flag: Int = -1, requestCode: Int = -1, crossinline callback: (result: Intent?) -> Unit = {}) {
    activity?.startActivityForResult<T>(*params, flag = flag, requestCode = requestCode, callback = callback)
}


fun FragmentActivity.returnAndFinish(vararg params: Pair<String, Any>) {
    setResult(Activity.RESULT_OK, Intent().putExtras(*params))
    finish()
}

fun Fragment.returnAndFinish(vararg params: Pair<String, Any>) {
    activity?.returnAndFinish(*params)
}
