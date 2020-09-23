package com.luuu.seven.module.read

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView



/**
 * Created by lls on 2017/8/7.
 */
interface ComicReadContract {

    interface Presenter : BasePresenter {
        fun getComicData(comicId: Int, chapterId: Int)
        fun updateReadHistory(comicId: Int, chapterId: Int, chapterTitle: String, browsePosition: Int,
                              cover: String, title: String)
    }

    interface View : BaseView<Presenter> {
        fun updateComicContent(urls: MutableList<String>, bytes: MutableList<ByteArray>?, isFromDisk: Boolean)
        fun isSuccess(isOver: Boolean)
    }
}