package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import com.luuu.seven.R
import com.luuu.seven.bean.HotComicBean
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
//                setText(R.id.tv_rank_pos, (adapterPosition + 3).toString())
                setText(R.id.comic_status, item?.status)
                setText(R.id.comic_type, item?.types)
                setText(R.id.comic_authors, item?.authors)
                getView<ImageView>(R.id.iv_rank_cover).loadWithHead(item?.cover ?: "")
            }
    }
}

class DiffRankCallback(newList: List<HotComicBean>) : BaseQuickDiffCallback<HotComicBean>(newList) {
    override fun areItemsTheSame(oldItem: HotComicBean, newItem: HotComicBean): Boolean {
        return oldItem.comicId == newItem.comicId
    }

    override fun areContentsTheSame(oldItem: HotComicBean, newItem: HotComicBean): Boolean {
        return oldItem.num == newItem.num && oldItem.lastUpdatetime == newItem.lastUpdatetime
    }

}