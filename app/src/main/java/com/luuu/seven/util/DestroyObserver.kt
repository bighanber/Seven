package com.luuu.seven.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DestroyObserver(val lifeOwner: LifecycleOwner) : LifecycleObserver {

    init {
        addObserver()
    }

    private lateinit var toRemoveListener: (DestroyObserver) -> Unit

    fun toRemove(listener: (DestroyObserver) -> Unit) {
        this.toRemoveListener = listener
    }

    private val mCompositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    private fun addObserver() {
        lifeOwner.lifecycle.addObserver(this)
    }

    fun addToDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    fun getObserverKey() = lifeOwner.toString()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        mCompositeDisposable.clear()
        lifeOwner.lifecycle.removeObserver(this)
        toRemoveListener.invoke(this)
    }
}