package com.luuu.seven.module.news

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.WebActivity
import com.luuu.seven.adapter.ComicNewsListAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.util.addTo
import com.luuu.seven.util.obtainViewModel
import kotlinx.android.synthetic.main.fra_tab_layout.*

/**
 * Created by lls on 2017/8/9.
 * 新闻界面
 */
class ComicNewsListFragment : BaseFragment() {


    private var mPageNum = 0
    private val mLayoutManager: LinearLayoutManager = LinearLayoutManager(mContext)
    private var mAdapter: ComicNewsListAdapter? = null
    private var mNewsListBeanList: ArrayList<ComicNewsListBean> = ArrayList()

    private lateinit var viewModel: NewsViewModel

    override fun initViews() {

        viewModel = obtainViewModel().apply {
            newsListData.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    mNewsListBeanList.addAll(it)
                    updateComicList(it)
                }

            })

            dataRefresh.observe(viewLifecycleOwner, Observer { bol ->
                bol?.let {
                    refresh.isEnabled = it
                }
            })

            dataLoadMore.observe(viewLifecycleOwner, Observer {  bol ->
                if (bol == null || !bol) {
                    mAdapter?.loadMoreComplete()
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
            mNewsListBeanList.clear()
            viewModel.getComicNewsList(mPageNum)
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout

    override fun onResume() {
        super.onResume()
        viewModel.getComicNewsList(mPageNum)
    }

    private fun updateComicList(data: List<ComicNewsListBean>) {

        when {
            mAdapter == null -> initAdapter()
            data.isEmpty() -> mAdapter?.loadMoreEnd()
            else -> {
                mAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun initAdapter() {
        mAdapter = ComicNewsListAdapter(R.layout.item_news_list_layout, mNewsListBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                viewModel.getComicNewsList(mPageNum)
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putString("url", mNewsListBeanList[position].pageUrl)
                startNewActivity(WebActivity::class.java, mBundle)
            }
        }
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter


    }

    private fun obtainViewModel(): NewsViewModel = obtainViewModel<NewsViewModel>()
}