package com.luuu.seven.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

/**
 * Created by lls on 2019-05-09
 */

fun AppCompatActivity.replaceFragment(fragment: Fragment, fragmentContent: Int) {
    supportFragmentManager.transact {
        replace(fragmentContent, fragment)
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProvider(this.viewModelStore, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(viewModelClass)

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProvider(this.viewModelStore, ViewModelProvider.AndroidViewModelFactory.getInstance(this.activity!!.application)).get(viewModelClass)