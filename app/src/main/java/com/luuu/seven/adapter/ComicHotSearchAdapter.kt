package com.luuu.seven.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.luuu.seven.R
import com.luuu.seven.bean.HotSearchBean
import com.luuu.seven.util.ifNotNull

/**
 * Created by lls on 2017/8/9.
 *
 */
class ComicHotSearchAdapter(layoutResId: Int, data: List<HotSearchBean>) :
        BaseQuickAdapter<HotSearchBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: HotSearchBean?) {
        ifNotNull(helper, item, {helper, item ->
            helper.setText(R.id.tv_hot, item.name)
            val params = helper.getView<TextView>(R.id.tv_hot).layoutParams
            if(params is FlexboxLayoutManager.LayoutParams) params.flexGrow = 1.0f
        })
    }
}