package com.luuu.seven.module.shelf

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicHistoryAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.util.nav
import kotlinx.android.synthetic.main.fra_shelf_list_layout.*

/**
 * Created by lls on 2017/8/9.
 * 历史界面
 */
class ComicHistoryFragment : BaseFragment() {

    private var mAdapter: ComicHistoryAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }

    private val viewModel: ShelfViewModel by viewModels()

    override fun initViews() {
        viewModel.historyData.observe(viewLifecycleOwner) { data ->
            data?.let {
                updateComic(it)
            }
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

        mAdapter?.setNewData(data) ?: initAdapter(data)
    }

    private fun initAdapter(historyBeanList: List<ReadHistoryBean>) {
        mAdapter = ComicHistoryAdapter(R.layout.item_shelf_layout, historyBeanList)
        recycler_shelf.layoutManager = mLayoutManager
        recycler_shelf.adapter = mAdapter

        mAdapter?.setOnItemClickListener { _, _, position ->

//            startActivity<ComicIntroActivity>(ComicIntroActivity.COMIC_ID to historyBeanList[position].comicId)
            nav().navigate(
                R.id.action_home_fragment_to_intro_fragment,
                Bundle().apply { putInt("comicId", historyBeanList[position].comicId) })
        }
    }

}