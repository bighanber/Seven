package com.luuu.seven.module.shelf.collect

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicCollectAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.CollectBean
import com.luuu.seven.module.intro.ComicIntroActivity
import kotlinx.android.synthetic.main.fra_shelf_list_layout.*

/**
 * Created by lls on 2017/8/9.
 * 收藏界面
 */
class ComicCollectFragment : BaseFragment(), CollectContract.View {

    private val mPresenter by lazy { CollectPresenter(this) }
    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }
    private var mAdapter: ComicCollectAdapter? = null

    override fun onFirstUserVisible() {
        mPresenter.getComicCollect()
    }

    override fun onUserInvisible() {
    }

    override fun onUserVisible() {
        mPresenter.getComicCollect()
    }

    override fun initViews() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
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

    override fun updateComicCollect(data: List<CollectBean>) {
        if (mAdapter == null) {
            initAdapter(data)
        } else {
            mAdapter!!.setNewData(data)
        }
    }

    private fun initAdapter(collectBeanList: List<CollectBean>) {
        mAdapter = ComicCollectAdapter(R.layout.item_search_layout, collectBeanList)
        recycler_shelf.layoutManager = mLayoutManager
        recycler_shelf.adapter = mAdapter
        mAdapter!!.setOnItemClickListener { _, _, position ->
            val mBundle = Bundle()
            mBundle.putInt("comicId", collectBeanList[position].comicId)
            startNewActivity(ComicIntroActivity::class.java, mBundle)
        }
    }
}