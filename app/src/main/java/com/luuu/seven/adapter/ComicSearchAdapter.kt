package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.SearchDataBean
import com.luuu.seven.util.ifNotNull
import com.luuu.seven.util.loadImg

/**
 * Created by lls on 2017/8/9.
 *  搜索界面列表适配器
 */
class ComicSearchAdapter(layoutResId: Int, data: List<SearchDataBean>) :
        BaseQuickAdapter<SearchDataBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: SearchDataBean?) {
        ifNotNull(helper, item, {helper, item ->
            helper.setText(R.id.tv_comic_title, item.title)
            helper.setText(R.id.tv_comic_authors, "作者: ${item.authors}")
            helper.setText(R.id.tv_comic_types, "类型: ${item.types}")
            helper.setText(R.id.tv_last_name, "最新: ${item.lastName}")
            helper.getView<ImageView>(R.id.iv_comic_cover).loadImg(item.cover)
        })
    }
}