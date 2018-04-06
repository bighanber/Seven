package com.luuu.seven.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.WebActivity
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.module.special.detail.ComicSpecialDetailActivity
import com.luuu.seven.util.ifNotNull

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:首页数据列表的适配器
 */
class ComicIndexAdapter(layoutResId: Int, data: List<IndexBean>) : BaseQuickAdapter<IndexBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: IndexBean?) {
        ifNotNull(helper, item, {helper, item ->
            helper.setText(R.id.tv_item_theme, item.title)
            val recyclerView = helper.getView<RecyclerView>(R.id.list_items)
            initOtherRecyclerView(recyclerView, item, mContext, helper)
        })
    }

    private fun initOtherRecyclerView(recyclerView: RecyclerView, images: IndexBean, context: Context, helper: BaseViewHolder) {
        val layoutId: Int
        val gridLayoutManager: GridLayoutManager
        if (images.sort == 4 || images.sort == 8 || images.sort == 10) {
            layoutId = R.layout.linear_item_layout
            gridLayoutManager = GridLayoutManager(context, 2)
        } else {
            layoutId = R.layout.grid_item_layout
            gridLayoutManager = GridLayoutManager(context, 3)
        }
        val imageAdapter = ComicIndexItemAdapter(layoutId, images.data)
        with(recyclerView) {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = imageAdapter
            isNestedScrollingEnabled = false
        }
        imageAdapter.setOnItemChildClickListener { _, _, position ->
            if (images.sort == 4) {
                if ("" == images.data[position].url) {
                    val mBundle = Bundle()
                    mBundle.putInt("tagId", images.data[position].objId)
                    mBundle.putString("title", images.data[position].subTitle)
                    val intent = Intent(mContext, ComicSpecialDetailActivity::class.java)
                    intent.putExtras(mBundle)
                    mContext.startActivity(intent)
                } else {
                    val mBundle = Bundle()
                    mBundle.putString("url", images.data[position].url)
                    val intent = Intent(mContext, WebActivity::class.java)
                    intent.putExtras(mBundle)
                    mContext.startActivity(intent)
                }
            } else {
                val mBundle = Bundle()
                mBundle.putInt("comicId", images.data[position].objId)
                val intent = Intent(mContext, ComicIntroActivity::class.java)
                intent.putExtras(mBundle)
                mContext.startActivity(intent)

            }
        }
    }
}