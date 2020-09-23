package com.luuu.seven.base

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:
 */
interface BaseView<T> {

//    fun setPresenter(presenter: T)
    fun showLoading(isLoading: Boolean)
    fun showError(isError: Boolean)
    fun showEmpty(isEmpty: Boolean)
}