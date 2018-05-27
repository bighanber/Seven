package com.luuu.seven.module.special

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.WebActivity
import com.luuu.seven.adapter.ComicSpecialAdapter
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.ComicSpecialBean
import com.luuu.seven.module.special.detail.ComicSpecialDetailActivity
import com.luuu.seven.util.DataLoadType
import kotlinx.android.synthetic.main.activity_comic_special.*

/***
 * 漫画专题
 *
 */
class ComicSpecialActivity : BaseActivity(), ComicSpecialContract.View {

    private val mPresenter by lazy { ComicSpecialPresenter(this) }
    private var mAdapter: ComicSpecialAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(this) }
    private var mSpecialBeanList: MutableList<ComicSpecialBean>? = null
    private var mPageNum = 0

    override fun initViews() {
        mSpecialBeanList = ArrayList()
        setToolbarTitle("漫画专题")
        refresh_special.setOnRefreshListener {
            mPageNum = 0
            mPresenter.refreshData()
        }
        mPresenter.getComicSpecial(0)
    }

    override fun getIntentExtras(extras: Bundle?) {
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_special

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
        mSpecialBeanList = null
    }


    override fun showLoading(isLoading: Boolean) {
    }

    override fun showError(isError: Boolean) {
    }

    override fun showEmpty(isEmpty: Boolean) {
    }

    override fun updateComicList(data: List<ComicSpecialBean>, type: DataLoadType) {
        when (type) {
            DataLoadType.TYPE_REFRESH_SUCCESS -> {
                mSpecialBeanList = data.toMutableList()
                if (mAdapter == null) {
                    initAdapter(data)
                } else {
                    mAdapter!!.setNewData(data)
                }
            }
            DataLoadType.TYPE_LOAD_MORE_SUCCESS ->
                if (data.isEmpty()) {
                    showToast(special_recycler,"没有数据咯")
                    mAdapter!!.loadMoreEnd()
                } else {
                    mAdapter!!.addData(data)
                    mAdapter!!.loadMoreComplete()
                    mSpecialBeanList!!.addAll(data)
                }
            else -> {
            }
        }
    }

    override fun judgeRefresh(isRefresh: Boolean) {
        refresh_special.isEnabled = isRefresh
    }

    private fun initAdapter(specialListBeanList: List<ComicSpecialBean>) {
        mAdapter = ComicSpecialAdapter(R.layout.item_special_layout, specialListBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                mPresenter.loadMoreData(mPageNum)
            }, special_recycler)
            setOnItemClickListener { _, _, position ->
                if (mSpecialBeanList!![position].pageType == 1) {
                    val mBundle = Bundle()
                    mBundle.putString("url", mSpecialBeanList!![position].pageUrl)
                    startNewActivity(WebActivity::class.java, mBundle)
                } else {
                    val mBundle = Bundle()
                    mBundle.putInt("tagId", mSpecialBeanList!![position].id)
                    mBundle.putString("title", mSpecialBeanList!![position].title)
                    startNewActivity(ComicSpecialDetailActivity::class.java, mBundle)
                }
            }
        }
        special_recycler.layoutManager = mLayoutManager
        special_recycler.adapter = mAdapter


    }
}
