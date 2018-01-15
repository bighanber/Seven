package com.luuu.seven.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.ComicSortBean
import com.luuu.seven.util.ifNotNull

/**
 * Created by lls on 2017/8/4.
 *  漫画分类框列表适配器
 */
class ComicSortDialogAdapter(layoutResId: Int, data: List<ComicSortBean>) :
        BaseQuickAdapter<ComicSortBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: ComicSortBean?) {
        ifNotNull(helper, item, {helper, item ->
            helper.setText(R.id.tv_num, item.title)
        })
    }

}