package com.luuu.seven.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.util.ifNotNull
import com.luuu.seven.util.loadImg

/**
 * Created by lls on 2017/8/4.
 *  漫画更新界面列表适配器
 */
class ComicUpdateAdapter(layoutResId: Int, data: List<ComicUpdateBean>) :
        BaseQuickAdapter<ComicUpdateBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: ComicUpdateBean?) {
        ifNotNull(helper, item, {helper, item ->
            helper.setText(R.id.tv_comic_title, item.title)
            helper.setText(R.id.tv_comic_chapter, item.lastUpdateChapterName)
            helper.getView<ImageView>(R.id.iv_photo_summary).loadImg(item.cover)
        })
    }
}