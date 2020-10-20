package com.luuu.seven.util.observer

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal object GlobalLifecycle : Lifecycle() {

    private val owner = LifecycleOwner { this }

    override fun addObserver(observer: LifecycleObserver) {
        require(observer is DefaultLifecycleObserver) {
            "$observer must implement androidx.lifecycle.DefaultLifecycleObserver."
        }

        observer.onCreate(owner)
        observer.onStart(owner)
        observer.onResume(owner)
    }

    override fun removeObserver(observer: LifecycleObserver) {
    }

    override fun getCurrentState(): State = State.RESUMED

    override fun toString(): String = "com.luuu.seven.util.observer.GlobalLifecycle"
}