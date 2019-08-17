package com.luuu.seven.module.shelf

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicHistoryAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.addTo
import com.luuu.seven.util.obtainViewModel
import com.luuu.seven.util.toast
import kotlinx.android.synthetic.main.fra_shelf_list_layout.*

/**
 * Created by lls on 2017/8/9.
 * 历史界面
 */
class ComicHistoryFragment : BaseFragment() {

    private var mAdapter: ComicHistoryAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }

    private lateinit var viewModel: ShelfViewModel

    override fun initViews() {
        viewModel = obtainViewModel().apply {
            historyData.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    updateComic(it)
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("asd", "his - onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("asd", "his - onResume")
        viewModel.getReadHistory()
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_shelf_list_layout

    private fun updateComic(data: List<ReadHistoryBean>) {
        if (mAdapter == null) {
            initAdapter(data)
        } else {
            mAdapter?.setNewData(data)
        }
    }

    private fun initAdapter(historyBeanList: List<ReadHistoryBean>) {
        mAdapter = ComicHistoryAdapter(R.layout.item_shelf_layout, historyBeanList)
        recycler_shelf.layoutManager = mLayoutManager
        recycler_shelf.adapter = mAdapter

        mAdapter?.setOnItemClickListener { _, _, position ->
            val mBundle = Bundle()
            mBundle.putInt("comicId", historyBeanList[position].comicId)
            startNewActivity(ComicIntroActivity::class.java, mBundle)
        }
    }

    private fun obtainViewModel(): ShelfViewModel = obtainViewModel(ShelfViewModel::class.java)
}