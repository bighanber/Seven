package com.luuu.seven.adapter

import android.text.format.DateFormat
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.ComicSortListBean
import com.luuu.seven.util.ifNotNull
import com.luuu.seven.util.loadWithHead

/**
 * Created by lls on 2017/8/4.
 *  漫画分类列表适配器
 */
class ComicSortListAdapter(layoutResId: Int, data: List<ComicSortListBean>) :
        BaseQuickAdapter<ComicSortListBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: ComicSortListBean?) {
        ifNotNull(helper, item, {helper, item ->
            helper.setText(R.id.tv_sort_list_title, item.title)
            helper.setText(R.id.tv_sort_list_authors, item.authors)
            helper.setText(R.id.tv_sort_list_types, item.types)
            helper.setText(R.id.tv_sort_list_status, item.status)
            helper.setText(R.id.tv_last_update_time, "最后更新: " + DateFormat.format("yyyy-MM-dd", item.lastUpdatetime * 1000))
            helper.getView<ImageView>(R.id.iv_sort_list_cover).loadWithHead(item.cover)
        })
    }

}