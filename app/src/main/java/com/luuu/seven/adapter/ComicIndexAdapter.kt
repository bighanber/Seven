package com.luuu.seven.adapter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.WebActivity
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.module.special.detail.ComicSpecialDetailActivity
import com.luuu.seven.widgets.SpaceItemDecoration

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:首页数据列表的适配器
 */
class ComicIndexAdapter(data: List<IndexBean>) : BaseQuickAdapter<IndexBean, ComicIndexAdapter.RecommendHolder>(R.layout.list_index_item_layout, data) {

//    val mPool = RecyclerView.RecycledViewPool()

    override fun convert(helper: RecommendHolder, item: IndexBean?) {
        helper.setText(R.id.tv_item_theme, item?.title)

        val layoutId: Int
        val gridLayoutManager: GridLayoutManager
        if (item?.sort == 5 || item?.sort == 9 || item?.sort == 11) {
            layoutId = R.layout.linear_item_layout
            gridLayoutManager = GridLayoutManager(mContext, 2)
        } else {
            layoutId = R.layout.grid_item_layout
            gridLayoutManager = GridLayoutManager(mContext, 3)
        }
        val imageAdapter = ComicIndexItemAdapter(layoutId, item?.data)

        with(helper.recommendRecycler) {
            layoutManager = gridLayoutManager
            adapter = imageAdapter
            isNestedScrollingEnabled = false
        }

        imageAdapter.setOnItemChildClickListener { _, _, position ->
            if (item?.sort == 5) {
                if (item.data[position].url.isEmpty()) {
                    val mBundle = Bundle()
                    mBundle.putInt("tagId", item.data[position].objId)
                    mBundle.putString("title", item.data[position].subTitle)
                    val intent = Intent(mContext, ComicSpecialDetailActivity::class.java)
                    intent.putExtras(mBundle)
                    mContext.startActivity(intent)
                } else {
                    val mBundle = Bundle()
                    mBundle.putString("url", item.data[position].url)
                    val intent = Intent(mContext, WebActivity::class.java)
                    intent.putExtras(mBundle)
                    mContext.startActivity(intent)
                }
            } else {
                val mBundle = Bundle()
                mBundle.putInt("comicId", item!!.data[position].objId)
                val intent = Intent(mContext, ComicIntroActivity::class.java)
                intent.putExtras(mBundle)
                mContext.startActivity(intent)

            }
        }
    }

    inner class RecommendHolder(view: View) : BaseViewHolder(view) {
        val recommendRecycler: RecyclerView = getView<RecyclerView>(R.id.list_items).apply {
            addItemDecoration(SpaceItemDecoration(mContext).setSpace(10))
//            setRecycledViewPool(mPool)
        }
    }
}