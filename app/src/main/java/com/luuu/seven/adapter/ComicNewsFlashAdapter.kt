package com.luuu.seven.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.http.Api
import com.luuu.seven.util.ifNotNull
import jp.wasabeef.glide.transformations.CropCircleTransformation


/**
 * Created by lls on 2017/8/9.
 * 快讯界面列表适配器
 */
class ComicNewsFlashAdapter(layoutResId: Int, data: List<ComicNewsFlashBean>) :
        BaseQuickAdapter<ComicNewsFlashBean, BaseViewHolder>(layoutResId, data) {
    
    override fun convert(helper: BaseViewHolder?, item: ComicNewsFlashBean?) {
        ifNotNull(helper, item, {helper, item ->
            helper.setText(R.id.tv_news_flash_nick, item.nickname)
            helper.setText(R.id.tv_news_flash_content, item.content)
            Glide.with(mContext).load(GlideUrl(item.cover, LazyHeaders.Builder().addHeader("Referer", Api.DMZJ).build()))
                    .bitmapTransform(CropCircleTransformation(mContext))
                    .into(helper.getView(R.id.iv_news_flash_img))
        })
    }
}