package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.util.ifNotNull
import com.luuu.seven.util.loadImg
import com.luuu.seven.util.loadWithHead


/**
 * Created by lls on 2017/8/4.
 *  排行界面列表适配器
 */
class ComicRankAdapter(layoutResId: Int, data: List<HotComicBean>) :
        BaseQuickAdapter<HotComicBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: HotComicBean?) {
            helper.apply {
                setText(R.id.tv_rank_title, item?.title)
                setText(R.id.tv_rank_num, item?.num.toString())
                setText(R.id.tv_rank_pos, (adapterPosition + 3).toString())
                getView<ImageView>(R.id.iv_rank_cover).loadWithHead(item?.cover ?: "")
            }
    }
}