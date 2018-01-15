package com.luuu.seven.module.shelf.history

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicHistoryAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.module.intro.ComicIntroActivity
import kotlinx.android.synthetic.main.fra_shelf_list_layout.*

/**
 * Created by lls on 2017/8/9.
 * 历史界面
 */
class ComicHistoryFragment : BaseFragment(), HistoryContract.View {

    private val mPresenter by lazy { HistoryPresenter(this) }
    private var mAdapter: ComicHistoryAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }


    override fun onFirstUserVisible() {
        mPresenter.getComicData()
    }

    override fun onUserInvisible() {
    }

    override fun onUserVisible() {
        mPresenter.getComicData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
    }

    override fun initViews() {
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_shelf_list_layout

    override fun onFirstUserInvisible() {
    }

    override fun showLoading(isLoading: Boolean) {
    }

    override fun showError(isError: Boolean) {
    }

    override fun showEmpty(isEmpty: Boolean) {
    }

    override fun updateComic(data: List<ReadHistoryBean>) {
        if (mAdapter == null) {
            initAdapter(data)
        } else {
            mAdapter!!.setNewData(data)
        }
    }

    private fun initAdapter(historyBeanList: List<ReadHistoryBean>) {
        mAdapter = ComicHistoryAdapter(R.layout.item_shelf_layout, historyBeanList)
        recycler_shelf.layoutManager = mLayoutManager
        recycler_shelf.adapter = mAdapter

        mAdapter!!.setOnItemClickListener { _, _, position ->
            val mBundle = Bundle()
            mBundle.putInt("comicId", historyBeanList[position].comicId)
            startNewActivity(ComicIntroActivity::class.java, mBundle)
        }
    }
}