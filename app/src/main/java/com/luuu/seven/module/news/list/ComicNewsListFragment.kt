package com.luuu.seven.module.news.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.R.id.recycler
import com.luuu.seven.R.id.refresh
import com.luuu.seven.WebActivity
import com.luuu.seven.adapter.ComicNewsListAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.util.DataLoadType
import kotlinx.android.synthetic.main.fra_tab_layout.*

/**
 * Created by lls on 2017/8/9.
 *新闻界面
 */
class ComicNewsListFragment : BaseFragment(), ComicNewsListContract.View {

    private var mPageNum = 0
    private val mPresenter by lazy { ComicNewsListPresenter(this) }
    private val mLayoutManager: LinearLayoutManager = LinearLayoutManager(mContext)
    private var mAdapter: ComicNewsListAdapter? = null
    private var mNewsListBeanList: MutableList<ComicNewsListBean>? = ArrayList()

    override fun onFirstUserVisible() {
    }

    override fun onUserInvisible() {
    }

    override fun onUserVisible() {
    }

    override fun initViews() {
        mPresenter.getComicData(0)
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                refresh.isEnabled = mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        })
        refresh.setOnRefreshListener {
            mPageNum = 0
            mPresenter.refreshData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
        mNewsListBeanList = null
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout

    override fun onFirstUserInvisible() {
    }

    override fun showLoading(isLoading: Boolean) {
    }

    override fun showError(isError: Boolean) {
    }

    override fun showEmpty(isEmpty: Boolean) {
    }

    override fun updateComicList(data: List<ComicNewsListBean>, type: DataLoadType) {
        when (type) {
            DataLoadType.TYPE_REFRESH_SUCCESS -> {
                mNewsListBeanList = data.toMutableList()
                if (mAdapter == null) {
                    initAdapter(data)
                } else {
                    mAdapter!!.setNewData(data)
                }
            }
            DataLoadType.TYPE_LOAD_MORE_SUCCESS -> if (data.isEmpty()) {
                showToast("没有数据咯")
                mAdapter!!.loadMoreEnd()
            } else {
                mAdapter!!.addData(data)
                mAdapter!!.loadMoreComplete()
                mNewsListBeanList!!.addAll(data)
            }
            else -> {
            }
        }
    }

    override fun judgeRefresh(isRefresh: Boolean) {
        refresh.isEnabled = isRefresh
    }

    private fun initAdapter(newsListBeen: List<ComicNewsListBean>) {
        mAdapter = ComicNewsListAdapter(R.layout.item_news_list_layout, newsListBeen).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                mPresenter.loadMoreData(mPageNum)
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putString("url", mNewsListBeanList!![position].pageUrl)
                startNewActivity(WebActivity::class.java, mBundle)
            }
        }
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter


    }
}