package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.ComicsSpecialDetInBean
import com.luuu.seven.util.ifNotNull
import com.luuu.seven.util.loadImg


/**
 * Created by lls on 2017/8/4.
 *  专题详情界面列表适配器
 */
class ComicSpecialDetailAdapter(layoutResId: Int, data: List<ComicsSpecialDetInBean> ) :
        BaseQuickAdapter<ComicsSpecialDetInBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: ComicsSpecialDetInBean?) {
        ifNotNull(helper, item, {helper, item ->
            helper.setText(R.id.tv_special_det_title, item.name)
            helper.setText(R.id.tv_special_det_brief, item.recommendBrief)
            helper.setText(R.id.tv_special_det_reason, item.recommendReason)
            helper.getView<ImageView>(R.id.iv_special_det_cover).loadImg(item.cover)
        })
    }
}