package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.ComicSpecialBean
import com.luuu.seven.util.loadWithHead


/**
 * Created by lls on 2017/8/4.
 *  专题界面列表适配器
 */
class ComicSpecialAdapter(layoutResId: Int, data: List<ComicSpecialBean>) :
        BaseQuickAdapter<ComicSpecialBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: ComicSpecialBean) {
        helper.setText(R.id.tv_special_title, item.title)
        helper.getView<ImageView>(R.id.iv_special_cover).loadWithHead(item.smallCover)
    }
}