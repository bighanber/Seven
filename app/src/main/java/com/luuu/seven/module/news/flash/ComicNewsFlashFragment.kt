package com.luuu.seven.module.news.flash

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicNewsFlashAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.util.DataLoadType
import kotlinx.android.synthetic.main.fra_tab_layout.*

/**
 * Created by lls on 2017/8/9.
 * 快讯界面
 */
class ComicNewsFlashFragment : BaseFragment(), ComicNewsFlashContract.View {

    private var mPageNum = 0
    private val mPresenter by lazy { ComicNewsFlashPresenter(this) }
    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }
    private var mAdapter: ComicNewsFlashAdapter? = null

    override fun onFirstUserVisible() {
    }

    override fun onUserInvisible() {
    }

    override fun onUserVisible() {
    }

    override fun initViews() {
        mPresenter.getComicData(0)
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
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

    override fun updateComicList(data: List<ComicNewsFlashBean>, type: DataLoadType) {
        when (type) {
            DataLoadType.TYPE_REFRESH_SUCCESS ->
                if (mAdapter == null) {
                    initAdapter(data)
                } else {
                    mAdapter!!.setNewData(data)
                }
            DataLoadType.TYPE_LOAD_MORE_SUCCESS ->
                if (data.isEmpty()) {
                    showToast("没有数据咯")
                    mAdapter!!.loadMoreEnd()
                } else {
                    mAdapter!!.addData(data)
                    mAdapter!!.loadMoreComplete()
                }
            else -> {
            }
        }
    }

    override fun judgeRefresh(isRefresh: Boolean) {
        refresh.isEnabled = isRefresh
    }

    private fun initAdapter(flashBeanList: List<ComicNewsFlashBean>) {
        mAdapter = with(ComicNewsFlashAdapter(R.layout.item_news_flash_layout, flashBeanList)) {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                mPresenter.loadMoreData(mPageNum)
            }, recycler)
            this
        }
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter
    }
}