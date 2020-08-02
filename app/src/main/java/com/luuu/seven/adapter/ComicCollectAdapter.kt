package com.luuu.seven.adapter

import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.CollectBean
import com.luuu.seven.util.loadWithHead

/**
 * Created by lls on 2017/8/9.
 * 收藏界面列表适配器
 */
class ComicCollectAdapter(layoutResId: Int, data: List<CollectBean>) :
    BaseQuickAdapter<CollectBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: CollectBean?) {

        helper.apply {
            setText(R.id.tv_comic_title, item?.comicTitle)
            setText(R.id.tv_comic_authors, item?.comicAuthors)
            setText(
                R.id.tv_last_name,
                "收藏时间: ${DateFormat.format("yyyy-MM-dd", item?.collectTime ?: 0L)}"
            )
            getView<TextView>(R.id.tv_comic_types).visibility = View.GONE
            getView<ImageView>(R.id.iv_comic_cover).loadWithHead(item?.comicCover ?: "")
        }
    }
}