package com.luuu.seven.adapter

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.module.index.HomeViewModel
import com.luuu.seven.widgets.SpaceItemDecoration

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:首页数据列表的适配器
 */
class ComicIndexAdapter(data: List<IndexBean>, val model: HomeViewModel) :
    BaseQuickAdapter<IndexBean, ComicIndexAdapter.RecommendHolder>(
        R.layout.list_index_item_layout,
        data
    ) {

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
            model.adapterClick(position, item!!)
        }
    }

    inner class RecommendHolder(view: View) : BaseViewHolder(view) {
        val recommendRecycler: RecyclerView = getView<RecyclerView>(R.id.list_items).apply {
            addItemDecoration(SpaceItemDecoration(mContext).setSpace(10))
//            setRecycledViewPool(mPool)
        }
    }
}