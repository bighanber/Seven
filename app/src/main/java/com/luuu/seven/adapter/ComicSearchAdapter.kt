package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.SearchDataBean
import com.luuu.seven.util.ifNotNull
import com.luuu.seven.util.loadImg
import com.luuu.seven.util.loadWithHead
import com.luuu.seven.util.string

/**
 * Created by lls on 2017/8/9.
 *  搜索界面列表适配器
 */
class ComicSearchAdapter(layoutResId: Int, data: List<SearchDataBean>) :
        BaseQuickAdapter<SearchDataBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: SearchDataBean?) {
        helper.apply {
            setText(R.id.tv_comic_title, item?.title)
            setText(R.id.tv_comic_authors, string(R.string.search_comic_authors, item?.authors))
            setText(R.id.tv_comic_types, string(R.string.search_comic_types, item?.types))
            setText(R.id.tv_last_name, string(R.string.search_comic_newest, item?.lastName))
            getView<ImageView>(R.id.iv_comic_cover).loadWithHead(item?.cover ?: "")
        }
    }
}