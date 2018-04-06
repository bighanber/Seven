package com.luuu.seven.util

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   :
 *     version:
 */
class SchedulerHelper {
    companion object {
        fun <T> io_main(): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

}