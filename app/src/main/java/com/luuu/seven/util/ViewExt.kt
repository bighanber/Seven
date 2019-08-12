package com.luuu.seven.util

import android.os.Looper
import android.view.View
import androidx.annotation.RestrictTo
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import java.util.concurrent.TimeUnit

val View.isVisible: Boolean
    get() = visibility == View.VISIBLE

val View.isInvisible: Boolean
    get() = visibility == View.INVISIBLE

val View.isGone: Boolean
    get() = visibility == View.GONE

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setVisible(show: Boolean) {
    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun <T : View> T.withTrigger(delay: Long = 600): T {
    triggerDelay = delay
    return this
}

fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {

    if (clickEnable()) {
        block(it as T)
    }
}

fun <T : View> T.clickWithTrigger(time: Long = 600, block: (T) -> Unit){
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(it as T)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

fun View.click(): Observable<Unit> {
    return ViewClickObservable(this)
}

private class ViewClickObservable(private val view: View) : Observable<Unit>() {

    override fun subscribeActual(observer: Observer<in Unit>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnClickListener(listener)
    }

    private class Listener(
        private val view: View,
        private val observer: Observer<in Unit>
    ) : MainThreadDisposable(), View.OnClickListener {

        override fun onClick(v: View) {
            if (!isDisposed) {
                observer.onNext(Unit)
            }
        }

        override fun onDispose() {
            view.setOnClickListener(null)
        }
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun checkMainThread(observer: Observer<*>): Boolean {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        observer.onSubscribe(Disposables.empty())
        observer.onError(IllegalStateException(
            "Expected to be called on the main thread but was " + Thread.currentThread().name))
        return false
    }
    return true
}

fun View.clickFilter(mCompositeDisposable: CompositeDisposable, callback: () -> Unit) {
    this.click().debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { callback.invoke() }.addTo(mCompositeDisposable)
}