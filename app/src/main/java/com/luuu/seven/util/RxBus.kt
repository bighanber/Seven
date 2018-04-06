package com.luuu.seven.util

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


/**
 * Created by lls on 2017/8/8.
 */
class RxBus {
    private var mBus: Subject<Any>? = null
    init {
        mBus = PublishSubject.create<Any>().toSerialized()
    }
    companion object {
        fun getInstance(): RxBus {
            return Single.Instance
        }
    }

    private object Single {
        val Instance = RxBus()
    }

    fun post(obj: Any) {
        mBus!!.onNext(obj)
    }

    fun <T> register(clz: Class<T>): Observable<T> {
        return mBus!!.ofType(clz)
    }

    fun unregisterAll() {
        //解除注册
        mBus!!.onComplete()
    }

    fun hasSubscribers(): Boolean {
        return mBus!!.hasObservers()
    }

}