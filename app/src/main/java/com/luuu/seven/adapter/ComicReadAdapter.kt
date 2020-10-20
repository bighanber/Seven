package com.luuu.seven.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.chrisbanes.photoview.PhotoView
import com.luuu.seven.R
import com.luuu.seven.util.loadWithHead

/**
 * Created by lls on 2018/9/1
 */
class ComicReadAdapter(
    data: List<String>
) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_read_list, data) {

    lateinit var tapCallBack: (Float, Float) -> Unit

    fun getTapBack(tapCallBack: (Float, Float) -> Unit) {
        this.tapCallBack = tapCallBack
    }

    override fun convert(helper: BaseViewHolder, item: String?) {
        helper.apply {
            getView<PhotoView>(R.id.read_view).loadWithHead(item ?: "")
            getView<PhotoView>(R.id.read_view).setOnPhotoTapListener { _, x, y ->
                tapCallBack.invoke(x, y)
            }
        }
    }
}