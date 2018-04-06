package com.luuu.seven.adapter

import android.text.format.DateFormat
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.util.ifNotNull
import com.luuu.seven.util.loadImg


/**
 * Created by lls on 2017/8/9.
 *  新闻界面列表适配器
 */
class ComicNewsListAdapter(layoutResId: Int, data: List<ComicNewsListBean>) :
        BaseQuickAdapter<ComicNewsListBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: ComicNewsListBean?) {
        ifNotNull(helper, item, {helper, item ->
            helper.setText(R.id.tv_news_list_title, item.title)
            helper.setText(R.id.tv_news_list_authors, item.nickname)
            helper.setText(R.id.tv_create_time, DateFormat.format("yyyy-MM-dd", item.createTime*1000))
            helper.getView<ImageView>(R.id.iv_news_list_cover).loadImg(item.rowPicUrl)
        })
    }
}