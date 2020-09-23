package com.luuu.seven.module.news

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicNewsFlashAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicNewsFlashBean
import kotlinx.android.synthetic.main.fra_tab_layout.*

/**
 * Created by lls on 2017/8/9.
 * 快讯界面
 */
class ComicNewsFlashFragment : BaseFragment() {


    private var mPageNum = 0
    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }
    private var mAdapter: ComicNewsFlashAdapter? = null
    private var mFlashList: ArrayList<ComicNewsFlashBean> = ArrayList()
    private val viewModel: NewsViewModel by viewModels()

    override fun initViews() {
        viewModel.apply {
            newsFlashData.observe(viewLifecycleOwner) { data ->
                data?.let {
                    mFlashList.addAll(it)
                    updateComicList(it)
                }

            }

            swipeRefreshing.observe(viewLifecycleOwner, {
                refresh.isEnabled = false
            })

            loadMore.observe(viewLifecycleOwner, {
                mAdapter?.loadMoreComplete()
            })

            isEmpty.observe(viewLifecycleOwner, {
                if (it) {
                    mAdapter?.loadMoreEnd()
                }
            })
        }
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                refresh.isEnabled = mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        })
        refresh.setOnRefreshListener {
            mPageNum = 0
            mFlashList.clear()
            viewModel.getComicNewsFlash(mPageNum, true)
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout

    override fun onResume() {
        super.onResume()
        viewModel.getComicNewsFlash(mPageNum)
    }

    private fun updateComicList(data: List<ComicNewsFlashBean>) {

        mAdapter?.notifyDataSetChanged() ?: initAdapter()
    }

    private fun initAdapter() {
        mAdapter = ComicNewsFlashAdapter(R.layout.item_news_flash_layout, mFlashList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                viewModel.getComicNewsFlash(mPageNum, more = true)
            }, recycler)
        }
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter
    }

}