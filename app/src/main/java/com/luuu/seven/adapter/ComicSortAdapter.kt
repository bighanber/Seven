package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.ComicSortListBean
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.util.ifNotNull
import com.luuu.seven.util.loadImg
import com.luuu.seven.util.loadWithHead

class ComicSortAdapter(data: List<ComicSortListBean>?) :
        BaseQuickAdapter<ComicSortListBean, BaseViewHolder>(R.layout.item_sort_layout, data) {

    override fun convert(helper: BaseViewHolder, item: ComicSortListBean?) {
        helper.apply {
            setText(R.id.comic_name, item?.title)
            getView<ImageView>(R.id.iv_photo_summary).loadWithHead(item?.cover ?: "")
        }
    }
}