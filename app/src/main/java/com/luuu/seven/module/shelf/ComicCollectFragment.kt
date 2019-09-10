package com.luuu.seven.module.shelf

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicCollectAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.CollectBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.addTo
import com.luuu.seven.util.obtainViewModel
import com.luuu.seven.util.toast
import kotlinx.android.synthetic.main.fra_shelf_list_layout.*

/**
 * Created by lls on 2017/8/9.
 * 收藏界面
 */
class ComicCollectFragment : BaseFragment() {


    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }
    private var mAdapter: ComicCollectAdapter? = null

    private lateinit var viewModel: ShelfViewModel

    override fun initViews() {
        viewModel = obtainViewModel<ShelfViewModel>().apply {

            collectData.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    updateComicCollect(it)
                }
            })
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_shelf_list_layout

    override fun onStart() {
        super.onStart()
        Log.e("asd", "coll - onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("asd", "coll - onResume")
        viewModel.getCollect()
    }


    private fun updateComicCollect(data: List<CollectBean>) {
        if (mAdapter == null) {
            initAdapter(data)
        } else {
            mAdapter?.setNewData(data)
        }
    }

    private fun initAdapter(collectBeanList: List<CollectBean>) {
        mAdapter = ComicCollectAdapter(R.layout.item_search_layout, collectBeanList)
        recycler_shelf.layoutManager = mLayoutManager
        recycler_shelf.adapter = mAdapter
        mAdapter?.setOnItemClickListener { _, _, position ->
            val mBundle = Bundle()
            mBundle.putInt("comicId", collectBeanList[position].comicId)
            startNewActivity(ComicIntroActivity::class.java, mBundle)
        }
    }
}