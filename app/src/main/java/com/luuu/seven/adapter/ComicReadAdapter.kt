package com.luuu.seven.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.util.ifNotNull
import com.luuu.seven.util.loadImgWithProgress

/**
 * Created by lls on 2018/9/1
 */
class ComicReadAdapter(data: List<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_read_list, data) {

    lateinit var tapCallBack: (Float, Float) -> Unit

    fun getTapBack(tapCallBack: (Float, Float) -> Unit) {
        this.tapCallBack = tapCallBack
    }

    override fun convert(helper: BaseViewHolder, item: String?) {
        ifNotNull(helper, item) { helper, item ->
//            helper.getView<PhotoView>(R.id.read_page).loadImgWithProgress(item, helper.getView(R.id.progress))
//            helper.getView<PhotoView>(R.id.read_page).setOnPhotoTapListener { view, x, y ->
//                tapCallBack.invoke(x, y)
//            }
        }
    }
}