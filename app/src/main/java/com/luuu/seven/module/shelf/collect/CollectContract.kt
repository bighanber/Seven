package com.luuu.seven.module.shelf.collect

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.CollectBean



/**
 * Created by lls on 2017/8/9.
 *
 */
interface CollectContract {
    interface Presenter : BasePresenter {
        fun getComicCollect()
    }

    interface View : BaseView<Presenter> {
        fun updateComicCollect(data: List<CollectBean>)
    }
}