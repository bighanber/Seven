package com.luuu.seven.module.news

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicNewsListAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.util.nav
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

    private val viewModel: NewsViewModel by viewModels()

    override fun initViews() {

        viewModel.apply {
            newsListData.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    mNewsListBeanList.addAll(it)
                    updateComicList()
                }
            })

            swipeRefreshing.observe(viewLifecycleOwner) {
                refresh.isEnabled = false
            }

            loadMore.observe(viewLifecycleOwner) {
                mAdapter?.loadMoreComplete()
            }

            isEmpty.observe(viewLifecycleOwner) {
                if (it) {
                    mAdapter?.loadMoreEnd()
                }
            }
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
            viewModel.getComicNewsList(mPageNum, true)
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout

    override fun onResume() {
        super.onResume()
        viewModel.getComicNewsList(mPageNum)
    }

    private fun updateComicList() {

        mAdapter?.notifyDataSetChanged() ?: initAdapter()
    }

    private fun initAdapter() {
        mAdapter = ComicNewsListAdapter(R.layout.item_news_list_layout, mNewsListBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                viewModel.getComicNewsList(mPageNum, more = true)
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putString("url", mNewsListBeanList[position].pageUrl)
//                startNewActivity(WebActivity::class.java, mBundle)
                nav().navigate(R.id.action_home_fragment_to_web_fragment, mBundle)
            }
        }
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter


    }

}