package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import com.luuu.seven.R
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.util.loadWithHead

/**
 * Created by lls on 2017/8/4.
 *  漫画更新界面列表适配器
 */
class ComicUpdateAdapter(layoutResId: Int, data: List<ComicUpdateBean>) :
        BaseQuickAdapter<ComicUpdateBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: ComicUpdateBean?) {
        helper.apply {
            setText(R.id.tv_comic_title, item?.title)
            setText(R.id.tv_comic_chapter, item?.lastUpdateChapterName)
            getView<ImageView>(R.id.iv_photo_summary).loadWithHead(item?.cover ?: "")
        }
    }
}

class DiffUpdateCallback(newList: List<ComicUpdateBean>) : BaseQuickDiffCallback<ComicUpdateBean>(newList) {
    override fun areItemsTheSame(oldItem: ComicUpdateBean, newItem: ComicUpdateBean): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ComicUpdateBean, newItem: ComicUpdateBean): Boolean {
        return oldItem.lastUpdateChapterName == newItem.lastUpdateChapterName
    }

}