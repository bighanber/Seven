package com.luuu.seven.util

import android.content.Context
import android.graphics.Rect
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RestrictTo
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
            }

            override fun onViewAttachedToWindow(v: View?) {
                v?.requestApplyInsets()
            }
        })
    }
}

fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, ViewPaddingState) -> Unit) {
    val paddingState = createStateForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, paddingState)
        insets
    }
    requestApplyInsetsWhenAttached()
}

data class ViewPaddingState(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
)

private fun createStateForView(view: View) = ViewPaddingState(view.paddingLeft,
    view.paddingTop, view.paddingRight, view.paddingBottom, view.paddingStart, view.paddingEnd)

fun paddingTop(context: Context): Int {
    val typedValue = TypedValue()
    val mActonBarHeight =
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true))
            TypedValue.complexToDimensionPixelSize(typedValue.data, context.resources.displayMetrics)
        else 0
    return BarUtils.getStatusBarHeight(context) + mActonBarHeight
}

fun EditText.values() = this.text.toString()

fun View.isRtl() = layoutDirection == View.LAYOUT_DIRECTION_RTL

/**
 * View有效区域
 */
fun View.isCover(): Boolean {
    var view = this
    val currentViewRect = Rect()
    val partVisible: Boolean = view.getLocalVisibleRect(currentViewRect)
    val totalHeightVisible =
        currentViewRect.bottom - currentViewRect.top >= view.measuredHeight
    val totalWidthVisible =
        currentViewRect.right - currentViewRect.left >= view.measuredWidth
    val totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible
    if (!totalViewVisible)
        return true
    while (view.parent is ViewGroup) {
        val currentParent = view.parent as ViewGroup
        if (currentParent.visibility != View.VISIBLE) //if the parent of view is not visible,return true
            return true

        val start = view.indexOfViewInParent(currentParent)
        for (i in start + 1 until currentParent.childCount) {
            val viewRect = Rect()
            view.getGlobalVisibleRect(viewRect)
            val otherView = currentParent.getChildAt(i)
            val otherViewRect = Rect()
            otherView.getGlobalVisibleRect(otherViewRect)
            if (Rect.intersects(viewRect, otherViewRect)) {
                //if view intersects its older brother(covered),return true
                return true
            }
        }
        view = currentParent
    }
    return false
}

fun View.indexOfViewInParent(parent: ViewGroup): Int {
    var index = 0
    while (index < parent.childCount) {
        if (parent.getChildAt(index) === this) break
        index++
    }
    return index
}
