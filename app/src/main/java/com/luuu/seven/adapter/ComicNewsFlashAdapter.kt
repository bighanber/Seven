package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.util.loadWithHead


/**
 * Created by lls on 2017/8/9.
 * 快讯界面列表适配器
 */
class ComicNewsFlashAdapter(layoutResId: Int, data: List<ComicNewsFlashBean>) :
        BaseQuickAdapter<ComicNewsFlashBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: ComicNewsFlashBean?) {
        helper.apply {
            getView<ImageView>(R.id.iv_news_flash_img).loadWithHead(item?.cover ?: "")
            setText(R.id.tv_news_flash_nick, item?.nickname)
            setText(R.id.tv_news_flash_content, item?.content)
        }
    }
}