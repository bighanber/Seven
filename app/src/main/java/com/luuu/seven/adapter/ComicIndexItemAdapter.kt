package com.luuu.seven.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.IndexDataBean
import com.luuu.seven.util.*


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:首页嵌套的数据列表适配器
 */
class ComicIndexItemAdapter(layoutResId: Int, data: List<IndexDataBean>?) : BaseQuickAdapter<IndexDataBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: IndexDataBean?) {
        helper.apply {
            setText(R.id.tv_grid_title, item?.title)
            addOnClickListener(R.id.grid_cardview)
            getView<ImageView>(R.id.iv_grid_img).loadWithHead(item?.cover ?: "")
            getView<TextView>(R.id.comic_state)?.let {
                if (item?.status.isNullOrEmpty()) {
                    it.gone()
                } else {
                    it.text = item?.status
                }
            }

        }

    }

}