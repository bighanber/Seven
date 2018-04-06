package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.IndexDataBean
import com.luuu.seven.util.ifNotNull
import com.luuu.seven.util.loadImg


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:首页嵌套的数据列表适配器
 */
class ComicIndexItemAdapter(layoutResId: Int, data: List<IndexDataBean>) : BaseQuickAdapter<IndexDataBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: IndexDataBean?) {
        ifNotNull(helper, item, {helper, item ->
            helper.setText(R.id.tv_grid_title, item.title)
            helper.addOnClickListener(R.id.grid_cardview)
            helper.getView<ImageView>(R.id.iv_grid_img).loadImg(item.cover)
        })
    }

}