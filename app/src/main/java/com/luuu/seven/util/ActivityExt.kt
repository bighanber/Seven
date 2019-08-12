package com.luuu.seven.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

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

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this).get(viewModelClass)

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this).get(viewModelClass)
