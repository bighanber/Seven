package com.luuu.seven.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luuu.seven.theme.Theme

/**
 * Created by lls on 2019-05-09
 */

fun AppCompatActivity.replaceFragment(fragment: Fragment, fragmentContent: Int) {
    supportFragmentManager.transact {
        replace(fragmentContent, fragment)
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, fragmentContent: Int) {
    supportFragmentManager.transact {
        add(fragmentContent, fragment)
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun AppCompatActivity.showFragment(fragment: Fragment) {
    supportFragmentManager.transact {
        show(fragment)
    }
}

fun AppCompatActivity.hideFragment(fragment: Fragment) {
    supportFragmentManager.transact {
        hide(fragment)
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
    }.commit()
}

inline fun <reified T : ViewModel> AppCompatActivity.obtainViewModel() =
    ViewModelProvider(this).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.obtainViewModel() =
    ViewModelProvider(this).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.activityViewModel() =
    ViewModelProvider(requireActivity()).get(T::class.java)

inline fun <reified T> Fragment.startActivity(flag: Int = -1, bundle: Bundle? = null) {
    activity?.startActivity<T>(flag, bundle)
}

inline fun <reified T> Context.startActivity(flag: Int = -1, bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java).apply {
        if (-1 != flag) {
            flags = flags
        } else if (this@startActivity !is Activity) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (bundle != null) putExtras(bundle)
    }
    startActivity(intent)
}
