package com.luuu.seven.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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